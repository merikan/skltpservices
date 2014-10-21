/**
 * Copyright (c) 2013 Center for eHalsa i samverkan (CeHis).
 * 							<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.adapterservices.apse.apsemedicalservicesadapteric.ticket;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.pascal.ticket.ArgosTicket;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.argos.ArgosHeader;
import se.skltp.adapterservices.apse.apsemedicalservicesadapteric.exception.TicketMachineException;

/**
 * Ticket machine provides possibilities to generate tickets that can be used
 * e.g when communicating with systems that demands this.
 */
public class TicketMachine {

	public static final String PRIVATPERSON = "PRIVATPERSON";
	
	private static final String WSSE_STARTTAG = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">";
	private static final String WSSE_ENDTAG = "</wsse:Security>";
	private static Logger log = LoggerFactory.getLogger(TicketMachine.class);
	private static ArgosTicket argosSamlTicketMachine;

	/**
	 * For testing the ticketmachine with e.g a mock
	 * 
	 * @param argosTicketMachine
	 */
	void setArgosTicketMachine(ArgosTicket argosTicketMachine) {
		argosSamlTicketMachine = argosTicketMachine;
	}

	private ArgosTicket getArgosTicketMachine() {
		if (argosSamlTicketMachine == null) {
			argosSamlTicketMachine = new ArgosTicket();
		}
		return argosSamlTicketMachine;
	}

	/**
	 * Get a SAML ticket based on ArgosHeader information.
	 * 
	 * @param argosHeader
	 *            The information used when generating the SAML ticket.
	 * @return A String representation of the SAML ticket in ws security format.
	 * @throws TicketMachineException
	 */
	public String produceSamlTicket(ArgosHeader argosHeader) throws TicketMachineException {
		try {
			String samlTicket = null;
			if(isTicketForCitizen(argosHeader)){
				log.info("Entering produce saml ticket for citizen request");
				log.debug("Argos header for citizen request {}", argosHeader);
				samlTicket = createCitizenTicket(argosHeader);
			}else{
				log.info("Entering produce saml ticket for organization request");
				log.debug("Argos header for organization request {}", argosHeader);
				samlTicket = createOrganizationTicket(argosHeader);
			}
			return applyWsSecurityToSamlTicket(samlTicket);
		} catch (Exception e) {
			throw new TicketMachineException("Exception generating saml ticket from ticket machine", e);
		} finally {
			log.info("Exiting produce saml ticket");
		}
	}

	/**
	 * Only when rollnamn contains exact string PRIVATPERSON the argosheader
	 * should be used for creating a citizen ticket. Described in Jira https://skl-tp.atlassian.net/browse/SKLTP-341.
	 * 
	 * @param argosHeader
	 * @return true if valid for citizen ticket
	 */
	static boolean isTicketForCitizen(final ArgosHeader argosHeader) {
		return argosHeader != null && argosHeader.getRollnamn() != null
				&& argosHeader.getRollnamn().contains(PRIVATPERSON);
	}

	private String createOrganizationTicket(ArgosHeader argosHeader) {
		return getArgosTicketMachine().getTicketForOrganization(argosHeader.getForskrivarkod(),
				argosHeader.getLegitimationskod(), argosHeader.getFornamn(), argosHeader.getEfternamn(),
				argosHeader.getYrkesgrupp(), argosHeader.getBefattningskod(), argosHeader.getArbetsplatskod(),
				argosHeader.getArbetsplatsnamn(), argosHeader.getPostort(), argosHeader.getPostadress(),
				argosHeader.getPostnummer(), argosHeader.getTelefonnummer(), argosHeader.getRequestId(),
				argosHeader.getRollnamn(), argosHeader.getHsaID(), argosHeader.getKatalog(),
				argosHeader.getOrganisationsnummer(), argosHeader.getSystemnamn(), argosHeader.getSystemversion(),
				argosHeader.getSystemIp());
	}

	private String createCitizenTicket(ArgosHeader argosHeader) {
		/*
		 * The construction of ssn = hsaid is described in Jira
		 * https://skl-tp.atlassian.net/browse/SKLTP-341. Short version is that
		 * there is no placeholder for ssn in ArgosHeader, therefore the field
		 * hsaid is used for carrying ssn for citizen services.
		 */
		String ssn = argosHeader.getHsaID();

		return getArgosTicketMachine().getTicketForCitizen(
				argosHeader.getFornamn(), argosHeader.getEfternamn(), ssn,
				argosHeader.getRollnamn(),
				argosHeader.getOrganisationsnummer(),
				argosHeader.getRequestId(), argosHeader.getSystemIp(),
				argosHeader.getSystemnamn(), argosHeader.getSystemversion());
	}

	/**
	 * Apply ws security information to saml ticket.
	 * 
	 * @param samlTicket
	 *            The ticket to apply ws security to
	 * @return The samlTicket including ws security information
	 */
	String applyWsSecurityToSamlTicket(String samlTicket) {
		if (StringUtils.isEmpty(samlTicket)) {
			return WSSE_STARTTAG + WSSE_ENDTAG;
		}
		return WSSE_STARTTAG + samlTicket + WSSE_ENDTAG;
	}

}
