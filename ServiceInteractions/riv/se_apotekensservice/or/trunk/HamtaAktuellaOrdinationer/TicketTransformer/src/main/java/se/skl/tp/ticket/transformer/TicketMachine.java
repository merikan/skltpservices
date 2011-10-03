/**
 * Copyright 2011 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.skl.tp.ticket.transformer;

import org.apache.commons.lang.StringUtils;

import se.inera.pascal.ticket.ArgosTicket;

/**
 * Ticket machine provides possibilities to generate tickets that can be used
 * e.g when communicating with systems that demands this.
 */
class TicketMachine {

    private static final String WSSE_STARTTAG = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">";
    private static final String WSSE_ENDTAG = "</wsse:Security>";

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
     */
    String produceSamlTicket(ArgosHeader argosHeader) {
	String samlTicket = getArgosTicketMachine().getTicket(argosHeader.getForskrivarkod(),
		argosHeader.getLegitimationskod(), argosHeader.getFornamn(), argosHeader.getEfternamn(),
		argosHeader.getYrkesgrupp(), argosHeader.getBefattningskod(), argosHeader.getArbetsplatskod(),
		argosHeader.getArbetsplatsnamn(), argosHeader.getPostort(), argosHeader.getPostadress(),
		argosHeader.getPostnummer(), argosHeader.getTelefonnummer(), argosHeader.getRequestId(),
		argosHeader.getRollnamn(), argosHeader.getHsaID(), argosHeader.getKatalog(),
		argosHeader.getOrganisationsnummer(), argosHeader.getSystemnamn(), argosHeader.getSystemversion(),
		argosHeader.getSystemIp());

	return applyWsSecurityToSamlTicket(samlTicket);
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
