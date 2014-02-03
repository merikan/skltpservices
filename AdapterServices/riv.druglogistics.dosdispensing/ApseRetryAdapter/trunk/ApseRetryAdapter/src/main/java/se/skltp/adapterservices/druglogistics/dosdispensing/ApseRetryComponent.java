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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApseRetryComponent {
	
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";

	private static final Logger log = LoggerFactory.getLogger(ApseRetryComponent.class);

	@SuppressWarnings("unused")
	private static int errors = 0;

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
	 * Main method of the component. Make a http(s) post-request.
	 * 
	 * @param request
	 */
	public String performPost(String request) {
		
		try {
			log.debug("Request: [{}]", request);

			HttpClientContext context    = HttpClientContext.create();
			HttpClient        httpClient = setupHttpClient();
			HttpPost          httpPost   = setupHttpPost(setupHttpRequestEntity(request));
			String            result     = performPostWithRetries(context, httpClient, httpPost);
			
			log.debug("Response: [{}]", result);
			return result;
			
		} catch (RuntimeException e) {
			if (e.getCause() == null) {
				log.debug("Call failed on runtime error: [{}]", e.toString());
			} else {
				log.debug("Call failed on runtime cause error: [{}]", e.getCause().toString());
			}
			throw e;
		}
	}

	/**
	 * Make a http(s) post-request with retry logic
	 * 
	 * @param context
	 * @param httpclient
	 * @param httpPost
	 * @return
	 */
	private String performPostWithRetries(HttpClientContext context, HttpClient httpclient, HttpPost httpPost)  {

		boolean ok = false;
		int retries = 0;
		String response = null;
		
		// Repeat until ok or maxRetries is passed and an exception is thrown
		do {
			try {
				System.err.println("Attempt: " + retries);
				response = attemptOnePostRequest(context, httpclient, httpPost);
				System.err.println("Attempt: " + retries + " OK!");
				ok = true;

			} catch (RuntimeException e) {
				System.err.println("Attempt: " + retries + " failed!");

				String errMsg = (e.getCause() == null) ? e.toString() : e.getCause().toString();
					
				if (retries == 0) {
					errors++;
					retries++;
					log.warn("First request failed, retrying... Error Message: {}", errMsg);

				} else if (retries < maxRetries) {
					retries++;
					log.warn("Retry " + retries + " failed, retrying... Error Message: {}", errMsg);

				} else {
					log.error("Request failed after {} retries. Giving up! Error Message: {}", retries, errMsg);
					throw e;
				}
			} finally {
				System.err.println("Attempt: " + retries + " pass finally");
			}
			
		} while (!ok);
		
		return response;
	}

	/**
	 * Try one http post request...
	 * 
	 * @param context
	 * @param httpclient
	 * @param httpPost
	 * @return
	 */
	private String attemptOnePostRequest(HttpClientContext context, HttpClient httpclient, HttpPost httpPost) {
		try {
			HttpResponse response = httpclient.execute(httpPost, context);
			String result = validateAndGetContent(response);
			return result; 
		} catch (Exception e) {
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

		logOkRequest("Status = " + retCode);
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

		if (retCode >= 400) {
			System.err.println("### HTTP ERROR CODE DETECTED, THROW ERROR");
			throw new RuntimeException("Bad status code: [" + retCode + "] with response [" + content + "]");
		}

		// Error checking for soapfault. Apoteket responds 200 OK , even for
		// soapfaults!
		if (content.contains("faultcode")) {
			System.err.println("### SOAP FAULT DETECTED IN OK RESPONSE, THROW ERROR");
			throw new RuntimeException("We got a SoapFault: [" + content + "] with status code [" + retCode + "]");
		}
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
	
	private static int okCnt = 0;

	private void logOkRequest(String msg) {
		okCnt++;
		log("OK: " + msg);
	}

	private static int errorCnt = 0;

	private void logErrorRequest(Exception e) {
		errorCnt++;
		log("ERROR: " + e.getMessage());
	}

	private void log(String msg) {

		int threadCnt = Thread.activeCount();
		System.out.println("\n\n" + new Date() + ": (" + threadCnt + "/" + okCnt + "/" + errorCnt + "): " + msg);
	}

	private static long getTs(long ts) {
		return System.currentTimeMillis() - ts;
	}

}
