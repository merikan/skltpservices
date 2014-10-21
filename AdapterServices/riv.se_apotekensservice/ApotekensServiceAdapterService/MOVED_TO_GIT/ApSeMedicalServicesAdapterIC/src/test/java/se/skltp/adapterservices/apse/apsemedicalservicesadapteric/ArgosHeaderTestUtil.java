package se.skltp.adapterservices.apse.apsemedicalservicesadapteric;

import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;

public class ArgosHeaderTestUtil {
	
	public static final ArgosHeaderType createOrganizationArgosHeader() {
		ArgosHeaderType argosHeader = new ArgosHeaderType();
		argosHeader.setArbetsplatskod("1234567890");
		argosHeader.setArbetsplatsnamn("Sjukhuset");
		argosHeader.setBefattningskod("123456");
		argosHeader.setEfternamn("Jansson");
		argosHeader.setFornamn("Ake");
		argosHeader.setForskrivarkod("1111129");
		argosHeader.setHsaID("TSE6565656565-1003");
		argosHeader.setKatalog("HSA");
		argosHeader.setLegitimationskod("1");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setPostadress("Vagen 1");
		argosHeader.setPostnummer("11111");
		argosHeader.setPostort("Staden");
		argosHeader.setRequestId("123456");
		argosHeader.setRollnamn("FORSKRIVARE");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		argosHeader.setTelefonnummer("08-1234567");
		argosHeader.setYrkesgrupp("Lakare");
		return argosHeader;
	}
	
	public static final ArgosHeaderType createCompleteCitizenArgosHeader() {
		ArgosHeaderType argosHeader = new ArgosHeaderType();
		argosHeader.setFornamn("Agda");
		argosHeader.setEfternamn("Andersson");
		argosHeader.setHsaID("188803099368");
		argosHeader.setRollnamn("PRIVATPERSON");
		argosHeader.setOrganisationsnummer("1234567890");
		argosHeader.setRequestId("123456");
		argosHeader.setSystemIp("192.0.0.1");
		argosHeader.setSystemnamn("Melior");
		argosHeader.setSystemversion("1.0");
		return argosHeader;
	}

}
