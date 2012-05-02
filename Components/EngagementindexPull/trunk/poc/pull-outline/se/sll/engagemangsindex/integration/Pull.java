package se.sll.engagemangsindex.integration;

import java.util.Map;
import java.security.KeyStore;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.ws.BindingProvider;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.GeneralSecurityException;

public class Pull implements Runnable {
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_ALGORITHM = "sunx509";

    private SSLContext sslContext;
    private String serviceCatalogEndpoint;
    private String engagementIndexEndpoint;
    private String producerEndpoint;
    private GetLogicalAddresseesByServiceContractResponderInterface port;
    
    //
    public Pull() {
    }

    public void run() {
	port.getLogicalAddresseesByServiceContract("", null);
    }

    public Pull withCertificate(String fileName, String password) {
	FileInputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            return withCertificate(stream, password);
	} catch (RuntimeException e) {
	    throw e;
        } catch (IOException e) {
            throw new RuntimeException("Invalid certificate file, or password: " + fileName, e);
        } finally {
            close(stream);
        }
    }

    private Pull withCertificate(InputStream inputStream, String password) throws IOException {
	if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must be specified");
        }

        return withSSLContext(newSSLContext(inputStream, password.toCharArray()));
    }

    public Pull withSSLContext(SSLContext sslContext) {
	this.sslContext = sslContext;
	return this;
    }

    public Pull withServiceCatalogEndpoint(String serviceCatalogEndpoint) {
	this.serviceCatalogEndpoint = serviceCatalogEndpoint;
	return this;
    }

    public Pull withEngagementIndexEndpoint(String engagementIndexEndpoint) {
	this.engagementIndexEndpoint = engagementIndexEndpoint;
	return this;
    }

    public Pull withProducerEndpoint(String producerEndpoint) {
	this.producerEndpoint = producerEndpoint;
	return this;
    }

    public Runnable build() {
	GetLogicalAddresseesByServiceContractResponderService service = new GetLogicalAddresseesByServiceContractResponderService();	
	this.port = service.getGetLogicalAddresseesByServiceContractResponderPort();
	BindingProvider provider = (BindingProvider)port;
	Map<String, Object> context = provider.getRequestContext();
	context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceCatalogEndpoint);
	context.put("com.sun.xml.ws.transport.https.client.SSLSocketFactory", sslContext.getSocketFactory());
	return this;
    }

    private void close(InputStream stream) {
	try {
	    if (stream != null) stream.close();
	} catch (Exception e) {}
    }
    

    private SSLContext newSSLContext(InputStream certInputStream, char[] password) throws IOException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(certInputStream, password);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_ALGORITHM);
            keyManagerFactory.init(keyStore, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KEY_ALGORITHM);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            return sslContext;
        } catch (GeneralSecurityException e) {
	    throw new RuntimeException("Invalid SSL configuration", e);
        }
    }

    public static void main(String[] args) {
	Runnable pull = new Pull().withServiceCatalogEndpoint(args[2]).withCertificate(args[0], args[1]).build();
	pull.run();
    }
}
