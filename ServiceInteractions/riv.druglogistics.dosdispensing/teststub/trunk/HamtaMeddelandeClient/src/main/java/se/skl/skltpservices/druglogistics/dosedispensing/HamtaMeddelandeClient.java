package se.skl.skltpservices.druglogistics.dosedispensing;

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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

public class HamtaMeddelandeClient {
	
	private static long sleepTime = 1000L;
	//private static String endpointAddress = "https://localhost:20001/loadtest/testproducer/GetSubjectOfCareSchedule/1/rivtabp21";
	private static String endpointAddress = "http://localhost:8088/mockHamtaMeddelandenResponderBinding";
//	private static String endpointAddress = "https://192.168.19.10:20000/vp/HamtaMeddelanden/1/rivtabp20";
	private static String keystorePath = "src/consumer.jks";
	private static String keystoreType = "JKS";
	private static String keystorePassword = "password";
	private static String truststorePath = "src/truststore.jks";
	private static String truststoreType = "JKS";
	private static String truststorePassword = "password";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
			
		if (args.length > 0) {
		      sleepTime = Long.parseLong(args[0]);
		      endpointAddress = args[1];
		      keystorePath = args[2];
		      keystoreType = args[3];
		      keystorePassword = args[4];
		      truststorePath = args[5];
		      truststoreType = args[6];
		      truststorePassword = args[7]; 
		}
				
//		System.setProperty("javax.net.debug", "ssl");
        final HttpEntity requestEntity = createRequest();
        final String expectedResponse = createExpectedResponse();
        
//		while (true) {
//			Thread.sleep(sleepTime);
//			new Thread(new Runnable() {public void run() {new HamtaMeddelandeClient().performHttpsPost(requestEntity, expectedResponse);}}).start();
//		}
        
        new HamtaMeddelandeClient().performHttpsPost(requestEntity, expectedResponse);
	}

	private static String createExpectedResponse() throws IOException {
//	InputStream expectedResponseStream = HamtaMeddelandeClient.class.getClassLoader().getResourceAsStream("expectedResponse.xml");
		InputStream expectedResponseStream = HamtaMeddelandeClient.class.getClassLoader().getResourceAsStream("expectedResponse2.xml");
        final String expectedResponse = streamToString(expectedResponseStream);
		return expectedResponse;
	}

	private static HttpEntity createRequest() throws IOException,
			UnsupportedEncodingException {
		//InputStream stream = HamtaMeddelandeClient.class.getClassLoader().getResourceAsStream("request.xml");
		InputStream stream = HamtaMeddelandeClient.class.getClassLoader().getResourceAsStream("request2.xml");
        String request = streamToString(stream);
        final HttpEntity requestEntity = new StringEntity(request);
		return requestEntity;
	}

	public void performHttpsPost(HttpEntity requestEntity, String expectedContent) {
		long ts = System.currentTimeMillis();
		long id = Thread.currentThread().getId();

        try {
        	
//        	Registry<ConnectionSocketFactory> socketFactoryRegistry = setupSSLSocketFactory();
        	
//            HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
//                    socketFactoryRegistry);

	        HttpClientContext context = HttpClientContext.create();

//	        HttpClient httpclient = HttpClients.custom()
//		        .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
//		        .setConnectionManager(connManager)
//		        .build();
	        
	        HttpClient httpclient = HttpClients.custom()
			        .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
			        .build();
	
			RequestConfig requestConfig = RequestConfig.custom()
	    		.setConnectTimeout(60000)
	    		.setSocketTimeout(60000)
	            .build();
	
	        HttpPost httpPost = new HttpPost(endpointAddress);
	        httpPost.setConfig(requestConfig);
	        httpPost.setEntity(requestEntity);
	
	        HttpResponse response = httpclient.execute(httpPost, context);
	
	        HttpEntity responseEntity = response.getEntity();
            int retCode = response.getStatusLine().getStatusCode();
            String content = getContent(responseEntity);

            if (!content.contains(expectedContent)) {
            	System.err.println("Expected response: [" + expectedContent + "]");
            	throw new RuntimeException("Unexpected response: [" + content + "]");
            } else {
            	System.err.println(content);
            }
            
            logOkRequest(ts, id, "Status = " + retCode);
        } catch (Exception e) {
			logErrorRequest(ts, id, e);
			e.printStackTrace();
		}		
	}

	private Registry<ConnectionSocketFactory> setupSSLSocketFactory() throws Exception,
			NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		KeyStore keystore = load(keystorePath, keystorePassword.toCharArray(), keystoreType);
		char[] keyPassword = keystorePassword.toCharArray();
		KeyStore truststore = load(truststorePath, truststorePassword.toCharArray(), truststoreType);

		SSLContext sslContext = SSLContexts.custom()
		        .loadKeyMaterial(keystore, keyPassword)
		        .loadTrustMaterial(truststore)
		        .build();
		
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
		    .register("https", new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))
		    .build();
		
//		  HttpParams params = new BasicHttpParams();
//		    SchemeRegistry registry = new SchemeRegistry();
//		    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//		    ClientConnectionManager cm = new ThreadSafeClientConnManager(params, registry);
		  
		
//		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//	    .register("http", new SingleConnectionManager())
//	    .build();

		
		return socketFactoryRegistry;
	}

	
	private KeyStore load(final String res, final char[] passwd, String type) throws Exception {
		final KeyStore keystore = KeyStore.getInstance(type);
		keystore.load(new FileInputStream(res), passwd);
		return keystore;
	}
			
	static public String streamToString(InputStream in) throws IOException {
		  StringBuilder out = new StringBuilder();
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  for(String line = br.readLine(); line != null; line = br.readLine()) 
		    out.append(line);
		  br.close();
		  return out.toString();
	}
	
	private static int okCnt = 0;
	public void logOkRequest(long ts, long id, String msg) {
		okCnt++;
        log(ts, id, "OK: " + msg);
	}

	private static int errorCnt = 0;
	public void logErrorRequest(long ts, long id, Exception e) {
		errorCnt++;
        log(ts, id, "ERROR: " + e.getMessage());
	}

	public void log(long ts, long id, String msg) {
		if (Thread.currentThread().getId() != id) {
			System.err.println("### ERROR THREAD ID INCORRECT");
		}

		int threadCnt = Thread.activeCount();
		System.out.println(new Date() + ": (" + threadCnt + "/" + okCnt + "/" + errorCnt + ") " + getTs(ts) + ": " + msg);
	}

	private static long getTs(long ts) {
		return System.currentTimeMillis() - ts;
	}

	public String getContent(HttpEntity entity) {
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
			if (in != null) try {in.close();} catch (IOException e) {}
		}
	}
}