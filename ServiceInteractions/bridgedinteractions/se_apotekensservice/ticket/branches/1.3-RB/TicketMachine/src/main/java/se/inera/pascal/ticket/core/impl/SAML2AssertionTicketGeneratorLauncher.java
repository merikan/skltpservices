package se.inera.pascal.ticket.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import se.inera.pascal.ticket.core.ApseAuthenticationAttributes;
import se.inera.pascal.ticket.core.ApseAuthorizationAttributes;
import se.inera.pascal.ticket.core.ApseInfoAttributes;
import se.inera.pascal.ticket.core.DefaultValues;
import se.inera.pascal.ticket.core.SAML2AssertionAttributeSet;
import se.inera.pascal.ticket.core.SAML2AssertionTicketGenerator;
import se.inera.pascal.ticket.core.SAML2Attribute;
import se.inera.pascal.ticket.core.StringConstants;

public class SAML2AssertionTicketGeneratorLauncher extends SAML2AssertionTicketGeneratorLauncherSupport {

	private static final Logger logger = LoggerFactory.getLogger(SAML2AssertionTicketGeneratorLauncher.class);

	private ApplicationContext appCtx = null;
	private SAML2AssertionTicketGenerator atg = null;
	private DefaultValues defVal = null;

	// starta applikationen och konfigurera alla beans
	public SAML2AssertionTicketGeneratorLauncher() throws Exception {
		logger.info("Launching  SAML2AssertionTicketGeneratorLauncher....");
		try {
			logger.debug("Loading SAML2AssertionTicketGenerator context....");
			appCtx = new ClassPathXmlApplicationContext(new String[] { "commandtool.xml" });
			atg = (SAML2AssertionTicketGenerator) appCtx.getBean("saml2AssertionTicketGenerator");
			defVal = (DefaultValues) appCtx.getBean("defaultValues");
			logger.info("SAML2AssertionTicketGeneratorLauncher launched and ready....");
		} catch (Exception e) {
			String errorString = "Failed initialize launcher application!";
			logger.error(errorString, e);
			throw new Exception(errorString);
		}
	}

	public String getValidToString() {
		return getValidToString(atg);
	}

	private List<SAML2Attribute> createSamlAuthenticationAttributes(final ApseAuthenticationAttributes authnAttr) {
		final List<SAML2Attribute> authnAttributes = new ArrayList<SAML2Attribute>();
		authnAttributes.add(create(StringConstants.ATTRIBUTE_AUTHENTICATION_DIRECTORYID, authnAttr.getDirectoryID()));
		authnAttributes.add(create(StringConstants.ATTRIBUTE_AUTHENTICATION_ORGANIZATIONID,
				authnAttr.getOrganisationID()));
		return authnAttributes;
	}

	private List<SAML2Attribute> createSamlInfoAttributes(final ApseInfoAttributes infoAttr) {
		final List<SAML2Attribute> infoAttributes = new ArrayList<SAML2Attribute>();
		infoAttributes.add(create(StringConstants.ATTRIBUTE_INFO_REQUESTID, infoAttr.getRequestID()));
		infoAttributes.add(create(StringConstants.ATTRIBUTE_INFO_SYSTEMNAME, infoAttr.getSystemNamn()));
		infoAttributes.add(create(StringConstants.ATTRIBUTE_INFO_SYSTEMVERSION, infoAttr.getSystemVersion()));
		infoAttributes.add(create(StringConstants.ATTRIBUTE_INFO_SYSTEMIP, infoAttr.getSystemIP()));
		return infoAttributes;
	}

	private List<SAML2Attribute> createSamlAuthorizationAttributes(final ApseAuthorizationAttributes autho) {

		final List<SAML2Attribute> authoAttributes = new ArrayList<SAML2Attribute>();

		String forskrivarkod = autho.getForskrivarkod();
		if (forskrivarkod != null && forskrivarkod.length() > 0) {
			authoAttributes
					.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_PRESCIBERCODE, autho.getForskrivarkod()));
		}

		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_ROLE, autho.getRollnamn()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_DIRECTORY_ID, autho.getKatalogId()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_DIRECTORY, autho.getKatalog()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_LEGITIMATIONCODE,
				autho.getLegitimationskod()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKCODE, autho.getYrkeskod()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_POSITIONCODE, autho.getBefattningskod()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_GIVENNAME, autho.getFornamn()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_FAMILYNAME, autho.getEfternamn()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKPLACECODE, autho.getArbetsplatskod()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_WORKPLACE, autho.getArbetsplats()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_ADDRESS, autho.getPostadress()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_ZIPCODE, autho.getPostnummer()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_POSTAREA, autho.getPostort()));
		authoAttributes.add(create(StringConstants.ATTRIBUTE_AUTHORIZATION_TELEPHONE, autho.getTelefonnummer()));

		return authoAttributes;
	}

	private SAML2Attribute create(final String name, final String value) {
		if (StringUtils.isEmpty(value) || "-".equals(value)) {
			return new SAML2Attribute(name, null);
		}
		return new SAML2Attribute(name, value);
	}

	public final String getTicket(final boolean getSecurityOnly, final ApseAuthorizationAttributes authoAttr,
			final ApseAuthenticationAttributes authnAttr, final ApseInfoAttributes infoAttr) {

		logger.info("Creating SAML ticket....");
		if (logger.isDebugEnabled()) {
			logger.debug("Create SAML ticket, getSecurityOnly: " + getSecurityOnly);
			logger.debug("Create SAML ticket, ApseAuthorizationAttributes: " + authoAttr);
			logger.debug("Create SAML ticket, ApseAuthenticationAttributes: " + authnAttr);
			logger.debug("Create SAML ticket, ApseInfoAttributes: " + infoAttr);
		}

		final SAML2AssertionAttributeSet attributeSet = new SAML2AssertionAttributeSet();
		attributeSet.setAuthorizationAttributes(createSamlAuthorizationAttributes(authoAttr));
		attributeSet.setAuthnAttributes(createSamlAuthenticationAttributes(authnAttr));
		attributeSet.setAuditingAttributes(createSamlInfoAttributes(infoAttr));

		final String message = getMessageAsString(atg, attributeSet, getSecurityOnly);
		logger.info("SAML ticket created....");

		if (defVal.getRemoveInitialXMLString().equalsIgnoreCase("true")
				|| defVal.getRemoveInitialXMLString().equalsIgnoreCase("yes")) {
			return message.replace(defVal.getXmlStringToRemove(), "");
		}

		return message;
	}
}
