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

	private final String trustStoreType = PropertyResolver.get("trustStoreType");
	private final String trustStoreFile = PropertyResolver.get("trustStoreFile");
	private final String trustStorePassword = PropertyResolver.get("trustStorePassword");
	private final String keyStoreType = PropertyResolver.get("keyStoreType");
	private final String keyStoreFile = PropertyResolver.get("keyStoreFile");
	private final String keyStorePassword = PropertyResolver.get("keyStorePassword");

	void configHttpConduit(Object service) {
		final Client clientProxy = ClientProxy.getClient(service);
		final HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();

		if (isHTTPS(conduit)) {
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
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
			} catch (NoSuchAlgorithmException e) {
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
			} catch (CertificateException e) {
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
			} catch (FileNotFoundException e) {
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
			} catch (IOException e) {
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
			} catch (UnrecoverableKeyException e) {
				log.warn("Fatal error occured when setting secuurity settings, make sure they are correct configured in app.properties file!");
				throw new RuntimeException("Fatal exception occured!", e);
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
