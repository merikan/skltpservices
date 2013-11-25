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
package se.skl.skltpservices.adapter.apse.ticket;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static se.skl.skltpservices.adapter.apse.ticket.TicketMachine.*;

import se.inera.pascal.ticket.ArgosTicket;
import se.skl.skltpservices.adapter.apse.argos.ArgosHeader;
import se.skl.skltpservices.adapter.apse.exception.TicketMachineException;
import se.skl.skltpservices.adapter.apse.ticket.TicketMachine;

public class TicketMachineTest {

    @Test
    public void testProduceCompleteSamlTicketFromArgosHeader() throws TicketMachineException {

	String forskrivarkod = "1111152";
	String legitimationskod = "1111111";
	String fornamn = "Lars";
	String efternamn = "Lakare";
	String yrkesgrupp = "FORSKRIVARE";
	String befattningskod = "1111111";
	String arbetsplatskod = "4000000000001";
	String arbetsplatsnamn = "VC Test";
	String postort = "Staden";
	String postadress = "Vagen 1";
	String postnummer = "11111";
	String telefonnummer = "0987654321";
	String requestId = "12345676";
	String rollnamn = "LK";
	String directoryID = "SE1111111111-1003";
	String hsaID = "SE1111111111-1003";
	String katalog = "HSA";
	String organisationsnummer = "111111111";
	String systemnamn = "Pascal";
	String systemversion = "1.0";
	String systemIp = "192.168.1.1";

	ArgosHeader argosHeader = new ArgosHeader(forskrivarkod, legitimationskod, fornamn, efternamn, yrkesgrupp,
		befattningskod, arbetsplatskod, arbetsplatsnamn, postort, postadress, postnummer, telefonnummer,
		requestId, rollnamn, directoryID, hsaID, katalog, organisationsnummer, systemnamn, systemversion,
		systemIp);

	String ticket = new TicketMachine().produceSamlTicket(argosHeader);
	

	assertThat(ticket, containsString("<saml2:Issuer>pascalonline</saml2:Issuer>"));
	assertThat(ticket, containsString("Lakare"));

    }

    @Test
    public void testApplyWsSecurityInformationToSamlTicket() {

	String samlTicket = "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\""
		+ "ID=\"_c4cda1f1c805c68a1a0c499ec6e34381\" IssueInstant=\"2011-09-28T06:32:56.395Z\""
		+ "Version=\"2.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>"
		+ "<saml2:Assertion xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\""
		+ "ID=\"_c4cda1f1c805c68a1a0c499ec6e34381\" IssueInstant=\"2011-09-28T06:32:56.395Z\""
		+ "Version=\"2.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
		+ "<saml2:Issuer>pascalonline</saml2:Issuer></saml2:Assertion>";

	String ticket = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket);

	assertNotNull(ticket);

	assertThat(
		ticket,
		containsString("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"));

	assertThat(ticket, containsString("</wsse:Security>"));
    }

    @Test
    public void testApplyWsSecurityEvenWhenSamlTicketDoesNoteExist() {
	String samlTicket = null;
	String samlTicketWithWsSecurity = new TicketMachine().applyWsSecurityToSamlTicket(samlTicket);

	assertThat(
		samlTicketWithWsSecurity,
		containsString("<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"></wsse:Security>"));
    }
    
    @Test
    public void validCitizenTicketRequest(){
    	ArgosHeader argosHeader = new ArgosHeader();
    	argosHeader.setRollnamn("PRIVATPERSON");
    	assertTrue(TicketMachine.isTicketForCitizen(argosHeader));
    }
    
    @Test
    public void whenRollnamnIsEmptyNotValidForCitizenTicket(){
    	ArgosHeader argosHeader = new ArgosHeader();
    	argosHeader.setRollnamn("");
    	assertFalse(TicketMachine.isTicketForCitizen(argosHeader));
    }
    
    @Test
    public void whenRollnamnIsNullNotValidForCitizenTicket(){
    	ArgosHeader argosHeader = new ArgosHeader();
    	argosHeader.setRollnamn(null);
    	assertFalse(TicketMachine.isTicketForCitizen(argosHeader));
    }
    
    @Test
    public void whenRollnamnIsNotPrivatPersonNotValidForCitizenTicket(){
    	ArgosHeader argosHeader = new ArgosHeader();
    	argosHeader.setRollnamn("OTHER");
    	assertFalse(TicketMachine.isTicketForCitizen(argosHeader));
    }
    
	@Test
	public void whenPrivatPersonCitizenTicketIsCalled() throws Exception{
		
		ArgosTicket argosTicket = mockArgosTicket();

		//Do the call
		ArgosHeader argosHeader = new ArgosHeader();
		argosHeader.setRollnamn(PRIVATPERSON);
		TicketMachine ticketMachine = new TicketMachine();
		ticketMachine.setArgosTicketMachine(argosTicket);
		ticketMachine.produceSamlTicket(argosHeader);
		
		//Verify
		verify(argosTicket, times(0)).getTicketForOrganization(anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString());
		
		verify(argosTicket, times(1)).getTicketForCitizen(anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString());

	}
	
    
	@Test
	public void whenNotPrivatPersonOrganizationTicketIsCalled() throws Exception{
		
		ArgosTicket argosTicket = mockArgosTicket();

		//Do the call
		ArgosHeader argosHeader = new ArgosHeader();
		argosHeader.setRollnamn("FORSKRIVARE");
		TicketMachine ticketMachine = new TicketMachine();
		ticketMachine.setArgosTicketMachine(argosTicket);
		ticketMachine.produceSamlTicket(argosHeader);
		
		//Verify
		verify(argosTicket, times(1)).getTicketForOrganization(anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString());
		
		verify(argosTicket, times(0)).getTicketForCitizen(anyString(),
				anyString(), anyString(), anyString(), anyString(),
				anyString(), anyString(), anyString(), anyString());

	}

	private ArgosTicket mockArgosTicket() {
		
		ArgosTicket argosTicket = mock(ArgosTicket.class);
		when(argosTicket
				.getTicketForOrganization(anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString(), anyString())).thenReturn("an organization ticket");
		when(argosTicket
				.getTicketForCitizen(anyString(),
						anyString(), anyString(), anyString(), anyString(),
						anyString(), anyString(), anyString(), anyString())).thenReturn("a citicen ticket");
		return argosTicket;
	}
    
    
    
}
