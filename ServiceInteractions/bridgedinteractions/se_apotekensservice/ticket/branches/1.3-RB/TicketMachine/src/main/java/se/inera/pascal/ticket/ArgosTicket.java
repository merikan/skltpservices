package se.inera.pascal.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.impl.SAML2AssertionTicketGeneratorLauncher;

public class ArgosTicket {

	private static Logger logger = LoggerFactory.getLogger(ArgosTicket.class);

	private static ArgosTicket instance;
	private SAML2AssertionTicketGeneratorLauncher launcher = null;
	private String launcherErrorString = "";

	private ArgosTicket() {
		logger.info("Launching  ArgosTicket....");
		try {
			launcher = new SAML2AssertionTicketGeneratorLauncher();
			logger.info("ArgosTicket launched....");
		} catch (Exception e) {
			launcher = null;
			launcherErrorString = e.getMessage();
			logger.error(launcherErrorString);
			throw new RuntimeException("Error launching ArgosTicket machine", e);
		}
	}

	public static ArgosTicket getInstance() {
		if (instance == null) {
			instance = new ArgosTicket();
		}
		return instance;
	}

	public String getTicket(String forskrivarkod, String legitimationskod, String fornamn, String efternamn,
			String Yrkesgrupp, String befattningskod, String arbetsplatskod, String arbetsplatsnamn, String postort,
			String postadress, String postnummer, String telefonnummer, String requestId, String rollnamn,
			String hsaID, String katalog, String organisationsnummer, String systemnamn, String systemversion,
			String systemIp) {

		if (launcher != null) {
			final ApseAuthorizationAttributes authoAttr = new ApseAuthorizationAttributes();
			authoAttr.setArbetsplats(arbetsplatsnamn);
			authoAttr.setArbetsplatskod(arbetsplatskod);
			authoAttr.setBefattningskod(befattningskod);
			authoAttr.setEfternamn(efternamn);
			authoAttr.setFornamn(fornamn);
			authoAttr.setForskrivarkod(forskrivarkod);
			authoAttr.setKatalog(katalog);
			authoAttr.setKatalogId(hsaID);
			authoAttr.setLegitimationskod(legitimationskod);
			authoAttr.setPostadress(postadress);
			authoAttr.setPostnummer(postnummer);
			authoAttr.setPostort(postort);
			authoAttr.setRollnamn(rollnamn);
			authoAttr.setTelefonnummer(telefonnummer);
			authoAttr.setYrkeskod(Yrkesgrupp);

			final ApseAuthenticationAttributes authnAttr = new ApseAuthenticationAttributes();
			authnAttr.setDirectoryID(hsaID);
			authnAttr.setOrganisationID(organisationsnummer);

			final ApseInfoAttributes infoAttr = new ApseInfoAttributes();
			infoAttr.setRequestID(requestId);
			infoAttr.setSystemIP(systemIp);
			infoAttr.setSystemNamn(systemnamn);
			infoAttr.setSystemVersion(systemversion);

			return launcher.getTicket(true, authoAttr, authnAttr, infoAttr);
		} else {
			return launcherErrorString;
		}
	}
}
