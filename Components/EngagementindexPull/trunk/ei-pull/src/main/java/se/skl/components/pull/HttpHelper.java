package se.skl.components.pull;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Service
public class HttpHelper {

	private final static Logger log = Logger.getLogger(HttpHelper.class);

	void configHttpConduit(Object service) {
		final Client clientProxy = ClientProxy.getClient(service);
		final HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();

		if (isHTTPS(conduit)) {

            final String trustStoreType = PropertyResolver.get("trustStoreType");
            final String trustStoreFile = PropertyResolver.get("trustStoreFile");
            final String trustStorePassword = PropertyResolver.get("trustStorePassword");
            final String keyStoreType = PropertyResolver.get("keyStoreType");
            final String keyStoreFile = PropertyResolver.get("keyStoreFile");
            final String keyStorePassword = PropertyResolver.get("keyStorePassword");

			try {
				final KeyStore trustStore = KeyStore.getInstance(trustStoreType);
				trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword.toCharArray());
				final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());

				final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
						.getDefaultAlgorithm());
				tmf.init(trustStore);

				final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(keyStore, keyStorePassword.toCharArray());

				final TLSClientParameters tlsCP = new TLSClientParameters();
				tlsCP.setTrustManagers(tmf.getTrustManagers());
				tlsCP.setKeyManagers(kmf.getKeyManagers());
				tlsCP.setDisableCNCheck(true);

				conduit.setTlsClientParameters(tlsCP);

			} catch (KeyStoreException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			} catch (NoSuchAlgorithmException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			} catch (CertificateException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			} catch (FileNotFoundException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			} catch (IOException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			} catch (UnrecoverableKeyException e) {
				log.warn("Fatal error occurred when setting security settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occurred!", e);
			}

		}
	}

	private boolean isHTTPS(HTTPConduit conduit) {
		if (conduit != null) {
			String targetAddr = conduit.getTarget().getAddress().getValue();
			return targetAddr.toLowerCase().startsWith("https:");
		}
		return false;
	}
}
