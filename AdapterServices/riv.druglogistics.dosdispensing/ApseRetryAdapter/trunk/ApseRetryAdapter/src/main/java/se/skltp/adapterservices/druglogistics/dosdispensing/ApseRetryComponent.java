package se.skltp.adapterservices.druglogistics.dosdispensing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApseRetryComponent {
	
	private static final String CONTENT_TYPE = "text/xml;charset=utf-8";

	private static final Logger log = LoggerFactory.getLogger(ApseRetryComponent.class);

	private static long sleepTime = 1000L;
	// private static String endpointAddress =
	// "https://localhost:20001/loadtest/testproducer/GetSubjectOfCareSchedule/1/rivtabp21";
	private static String endpointAddress = "https://localhost:8082/ApseRetryAdapter/services/hamtaMeddelande-soap-teststub/v1";
	// private static String endpointAddress =
	// "https://192.168.19.10:20000/vp/HamtaMeddelanden/1/rivtabp20";
	private static String keystorePath = "src/consumer.jks";
	private static String keystoreType = "JKS";
	private static String keystorePassword = "password";
	private static String truststorePath = "src/truststore.jks";
	private static String truststoreType = "JKS";
	private static String truststorePassword = "password";
	private static int errors = 0;

	private static int maxRetries = 3;
	
	public String performPost(String request) throws Exception{
		
		log.debug("Request: [{}]", request);
		
		HttpEntity requestEntity;
		try {
			requestEntity = new StringEntity(request, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error creating HttpEntity from request", e);
		}
		return performPost(requestEntity, 0);
	}

	private String performPost(HttpEntity requestEntity, int retries) throws Exception{
		
		if (endpointAddress.startsWith("https")) {
			return performHttpsPost(requestEntity, retries);
		} else {
			return performHttpPost(requestEntity, retries);
		}
	}

	/**
	 * Make a http-request
	 * 
	 * @param requestEntity
	 * @param retries
	 */
	private String performHttpPost(HttpEntity requestEntity, int retries) throws Exception {
		long ts = System.currentTimeMillis();
		long id = Thread.currentThread().getId();

		try {

			HttpClientContext context = HttpClientContext.create();

			HttpClient httpclient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
					.build();

			HttpPost httpPost = new HttpPost(endpointAddress);
			httpPost.setEntity(requestEntity);
			httpPost.setHeader("Content-Type", CONTENT_TYPE);
			httpPost.setHeader("SOAPAction",
					"urn:riv:druglogistics:dosedispensing:HamtaMeddelandenResponder:1:HamtaMeddelanden");

			HttpResponse response = httpclient.execute(httpPost, context);

			HttpEntity responseEntity = response.getEntity();
			int retCode = response.getStatusLine().getStatusCode();
			String content = getContent(responseEntity);

			// Check for errors
			validate(retCode, content);

			logOkRequest(ts, id, "Status = " + retCode);
			
			return content;
			
		} catch (Exception e) {

			logErrorRequest(ts, id, e);
			e.printStackTrace();

			if (retries == 0) {

				errors++;

				System.err.println("First request failed. Retrying...");
				return performPost(requestEntity, ++retries);

			} else if (retries < maxRetries) {
				System.err.println("Retry " + retries + " failed. Retrying...");
				return performPost(requestEntity, ++retries);

			} else {
				System.out.println("Request failed. Giving up....");
				throw e;
			}

		}
	}

	/**
	 * Make a https-request
	 * 
	 * @param requestEntity
	 * @param retries
	 */
	private String performHttpsPost(HttpEntity requestEntity, int retries) throws Exception {
		
		
		long ts = System.currentTimeMillis();
		long id = Thread.currentThread().getId();

		try {

			Registry<ConnectionSocketFactory> socketFactoryRegistry = setupSSLSocketFactory();

			HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);

			HttpClientContext context = HttpClientContext.create();

			HttpClient httpclient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
					.setConnectionManager(connManager).build();

			HttpPost httpPost = new HttpPost(endpointAddress);
			httpPost.setEntity(requestEntity);
			httpPost.setHeader("Content-Type", CONTENT_TYPE);
			httpPost.setHeader("SOAPAction",
					"urn:riv:druglogistics:dosedispensing:HamtaMeddelandenResponder:1:HamtaMeddelanden");

			HttpResponse response = httpclient.execute(httpPost, context);

			HttpEntity responseEntity = response.getEntity();
			int retCode = response.getStatusLine().getStatusCode();
			String content = getContent(responseEntity);

			// Check for errors
			validate(retCode, content);

			logOkRequest(ts, id, "Status = " + retCode);
			
			return content;
			
		} catch (Exception e) {

			logErrorRequest(ts, id, e);
			e.printStackTrace();

			if (retries == 0) {

				errors++;

				System.err.println("First request failed. Retrying...");
				return performPost(requestEntity, ++retries);

			} else if (retries < maxRetries) {
				System.err.println("Retry " + retries + " failed. Retrying...");
				return performPost(requestEntity, ++retries);

			} else {
				System.out.println("Request failed. Giving up....");
				throw e;
			}

		}
	}

	private Registry<ConnectionSocketFactory> setupSSLSocketFactory() throws Exception, NoSuchAlgorithmException,
			KeyManagementException, KeyStoreException, UnrecoverableKeyException {
		KeyStore keystore = load(keystorePath, keystorePassword.toCharArray(), keystoreType);
		char[] keyPassword = keystorePassword.toCharArray();
		KeyStore truststore = load(truststorePath, truststorePassword.toCharArray(), truststoreType);

		SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, keyPassword)
				.loadTrustMaterial(truststore).build();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register(
						"https",
						new SSLConnectionSocketFactory(sslContext,
								SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)).build();

		return socketFactoryRegistry;
	}

	private KeyStore load(final String res, final char[] passwd, String type) throws Exception {
		final KeyStore keystore = KeyStore.getInstance(type);
		keystore.load(new FileInputStream(res), passwd);
		return keystore;
	}

	static private String streamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (String line = br.readLine(); line != null; line = br.readLine())
			out.append(line);
		br.close();
		return out.toString();
	}

	private static int okCnt = 0;

	private void logOkRequest(long ts, long id, String msg) {
		okCnt++;
		log(ts, id, "OK: " + msg);
	}

	private static int errorCnt = 0;

	private void logErrorRequest(long ts, long id, Exception e) {
		errorCnt++;
		log(ts, id, "ERROR: " + e.getMessage());
	}

	private void log(long ts, long id, String msg) {
		if (Thread.currentThread().getId() != id) {
			System.err.println("### ERROR THREAD ID INCORRECT");
		}

		int threadCnt = Thread.activeCount();
		System.out.println("\n\n" + new Date() + ": (" + threadCnt + "/" + okCnt + "/" + errorCnt + ") " + getTs(ts)
				+ ": " + msg);
	}

	private static long getTs(long ts) {
		return System.currentTimeMillis() - ts;
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
			throw new RuntimeException("Bad status code: [" + retCode + "] with response [" + content + "]");
		}

		// Error checking for soapfault. Apoteket responds 200 OK , even for
		// soapfaults!
		if (content.contains("faultcode")) {
			throw new RuntimeException("We got a SoapFault: [" + content + "] with status code [" + retCode + "]");
		}
	}
}
