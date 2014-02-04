package se.skltp.adapterservices.druglogistics.dosdispensing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.mule.api.MuleContext;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.logentry.schema.v1.LogLevelType;
import org.soitoolkit.commons.mule.api.log.EventLogMessage;
import org.soitoolkit.commons.mule.api.log.EventLogger;
import org.soitoolkit.commons.mule.log.DefaultEventLogger;
import org.soitoolkit.commons.mule.log.EventLoggerFactory;

public class ApseRetryComponent implements Callable, MuleContextAware {
	
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";

	private static final Logger log = LoggerFactory.getLogger(ApseRetryComponent.class);

	@SuppressWarnings("unused")
	private static int errors = 0;

	private MuleContext context = null;
	private EventLogger eventLogger = null;
		
	@Override
	public void setMuleContext(MuleContext context) {
		this.context = context;
	}

	/*
	 * Configurable properties
	 */
	
	private String endpointAddress = null;
	public void setEndpointAddress(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	private int maxRetries = -1;
	public void setMaxRetries(int maxRetries) {
		log.debug("Setting MaxRetries: [{}]", maxRetries);
		this.maxRetries = maxRetries;
	}

	private int retryWait = 0;
	public void setRetryWait(int retryWait) {
		log.debug("Setting RetryWait: [{}]", retryWait);
		this.retryWait = retryWait;
	}

	private int timeout = -1;
	public void setTimeout(int timeout) {
		log.debug("Setting Timeout: [{}]", timeout);
		this.timeout = timeout;
	}
	
	private String soapAction = null;
	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	private String keystorePath = null;
	public void setKeystorePath(String keystorePath) {
		this.keystorePath = keystorePath;
	}

	private String keystoreType = null;
	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}

	private String keystorePassword = null;
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	private String truststorePath = null;
	public void setTruststorePath(String truststorePath) {
		this.truststorePath = truststorePath;
	}

	private String truststoreType = null;
	public void setTruststoreType(String truststoreType) {
		this.truststoreType = truststoreType;
	}

	private String truststorePassword = null;
	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}


	/**
	 * Main method of the component. Make a http(s) post-request and retries a number of times before giving up. If the request casue a soap-fault it is sent back as the response (but with no handling of http-status, e.g. setting it to 500)
	 * 
	 * @param eventContext
	 */
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {

		// Get the request from the mule message
		MuleMessage muleMessage = eventContext.getMessage();
		String      request     = muleMessage.getPayload().toString();
		
		// Setup HTTP stuff
		HttpClientContext context    = HttpClientContext.create();
		HttpClient        httpClient = setupHttpClient();
		HttpPost          httpPost   = setupHttpPost(setupHttpRequestEntity(request));

		// Perform the processing including retries
		log.debug("Request: [{}]", request);
		String response = performPostWithRetries(muleMessage, context, httpClient, httpPost);
		log.debug("Response: [{}]", response);
		
		// Set response and bail out
		muleMessage.setPayload(response);
		return muleMessage;
	}

	/**
	 * Make a http(s) post-request with retry logic
	 * 
	 * @param context
	 * @param httpclient
	 * @param httpPost
	 * @return
	 */
	private String performPostWithRetries(MuleMessage muleMessage, HttpClientContext context, HttpClient httpclient, HttpPost httpPost)  {

		boolean ok = false;
		int retries = 0;
		String response = null;
		
		// Repeat until ok or maxRetries is passed and an exception is thrown
		do {
			try {
				log.debug("Attempt #{}", retries);
				response = attemptOnePostRequest(context, httpclient, httpPost);
				log.debug("Attempt #{} OK!", retries);
				ok = true;

			} catch (RuntimeException e) {
				log.debug("Attempt #{} failed!", retries);

				// First call failed
				if (retries == 0) {
					errors++;
					retries++;
					logWarning(e, "First request failed, retrying...", muleMessage);
					if (retryWait > 0) waitBeforeNextRetry();

				// A retry call failed
				} else if (retries < maxRetries) {
					retries++;
					logWarning(e, "Retry " + retries + " failed, retrying...", muleMessage);
					if (retryWait > 0) waitBeforeNextRetry();

				// The last retry call failed, time to give up
				} else {
					
					if (e instanceof SoapFaultInPayload) {
						SoapFaultInPayload sfe = (SoapFaultInPayload)e;
						logError(e, "Request failed after " + retries + " retries. Giving up! SOAP Fault from producer passed back to consumer", muleMessage);
						muleMessage.setOutboundProperty("http.status", 500); // FIXME Not so nice but here is a good place to set the http  return code for the soap fault 
						return sfe.getSoapFault();

					} else {
						log.error("Request failed after " + retries + " retries. Giving up!");
						throw e;
					}
				}
			}
			
		} while (!ok);
		
		return response;
	}

	private void waitBeforeNextRetry() {
		log.warn("Sleep for a short while before retrying: {} ms", retryWait);
		try {
			Thread.sleep(retryWait);
		} catch (InterruptedException e) {}
		log.warn("Sleep is over, let's give it a new try...");
	}


	/**
	 * Try one http post request...
	 * 
	 * @param context
	 * @param httpclient
	 * @param httpPost
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private String attemptOnePostRequest(HttpClientContext context, HttpClient httpclient, HttpPost httpPost) {
		try {
			HttpResponse response = httpclient.execute(httpPost, context);
			String result = validateAndGetContent(response);
			return result; 
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Validate the response and get the content
	 * 
	 * @param response
	 * @return
	 */
	private String validateAndGetContent(HttpResponse response) {
		HttpEntity responseEntity = response.getEntity();
		int retCode = response.getStatusLine().getStatusCode();
		String content = getContent(responseEntity);

		// Check for errors
		validate(retCode, content);

		return content;
	}

	private String getContent(HttpEntity entity) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String inputLine;
			StringBuffer content = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			return content.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}

	private void validate(int retCode, String content) {

		// First check for soapfaults
		//
		// Error checking for soapfault. Apoteket responds 200 OK , even for soapfaults!
		if (isPayloadSoapFault(content)) {
			log.debug("SOAP Fault detected in response, throw error");
			SoapFaultInPayload sfe = new SoapFaultInPayload("We got a SoapFault: [" + content + "] with status code [" + retCode + "]");
			sfe.setSoapFault(content);
			throw sfe;
		}

		// Then check for other errors
		if (retCode >= 400) {
			log.debug("HTTP error code detected in response, throw error");
			throw new RuntimeException("Bad status code: [" + retCode + "] with response [" + content + "]");
		}

	}


	private boolean isPayloadSoapFault(String content) {
		// FIXME: Make a better assert of soap-fault, e.g. check for valid xml and expected elements including namespace...
		return content.contains("faultcode");
	}

	/*
	 * Setup methods
	 */

	private StringEntity setupHttpRequestEntity(String request) {
		try {
			return new StringEntity(request, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private HttpPost setupHttpPost(HttpEntity requestEntity) {
		HttpPost httpPost = new HttpPost(endpointAddress);

		if (timeout > 0) {
			log.debug("Setup connect and request timeout to {} ms.", timeout);
			RequestConfig requestConfig = RequestConfig.custom()
		    		.setConnectTimeout(timeout)
		    		.setSocketTimeout(timeout)
		            .build();
			httpPost.setConfig(requestConfig);
		}
		
		httpPost.setEntity(requestEntity);
		httpPost.setHeader("Content-Type", CONTENT_TYPE);
		httpPost.setHeader("SOAPAction", soapAction);
		return httpPost;
	}

	private HttpClient setupHttpClient() {

		HttpClientBuilder httpClientBuilder = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
		if (endpointAddress.toLowerCase().startsWith("https")) {
			httpClientBuilder = httpClientBuilder.setConnectionManager(setupSSLConnectionManager());
		}
		
		HttpClient httpclient = httpClientBuilder.build();
		return httpclient;
	}

	private HttpClientConnectionManager setupSSLConnectionManager() {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = setupSSLSocketFactory();

		HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
		return connManager;
	}

	private Registry<ConnectionSocketFactory> setupSSLSocketFactory() {
		try {
			KeyStore keystore   = loadKeystore(keystorePath, keystorePassword.toCharArray(), keystoreType);
			char[] keyPassword  = keystorePassword.toCharArray();
			KeyStore truststore = loadKeystore(truststorePath, truststorePassword.toCharArray(), truststoreType);

			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, keyPassword).loadTrustMaterial(truststore).build();

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("https", new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)).build();

			return socketFactoryRegistry;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private KeyStore loadKeystore(final String res, final char[] passwd, String type) {
		try {
			final KeyStore keystore = KeyStore.getInstance(type);
			keystore.load(new FileInputStream(res), passwd);
			return keystore;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * Logger methods
	 */
	// TODO: Cleanup the methods below, we don't need most of it!
	
	public EventLogger getEventLogger() {
		if (eventLogger == null) {
			CustomEventLogger cel = new CustomEventLogger();
			cel.setMuleContext(context);
			eventLogger = cel;
		}
		return eventLogger;
	}

	private void logWarning(Exception e, String logMessage, MuleMessage mm) {
		EventLogMessage elm = new EventLogMessage();
		elm.setMuleMessage(mm);
		elm.setLogMessage(logMessage);
		getEventLogger().logErrorEvent(LogLevelType.WARNING, e, elm);                
	}
	private void logError(Exception e, String logMessage, MuleMessage mm) {
		EventLogMessage elm = new EventLogMessage();
		elm.setMuleMessage(mm);
		elm.setLogMessage(logMessage);
		getEventLogger().logErrorEvent(e, elm);                
	}

}
