package se.skl.tp.ticket.testproducer;

import javax.jws.WebService;

import riv.se_apotekensservice.or._1.ApoteksinformationResponse;
import riv.se_apotekensservice.or._1.ArbetsplatsInfoResponse;
import riv.se_apotekensservice.or._1.ArtikelResponse;
import riv.se_apotekensservice.or._1.DosunderlagResponse;
import riv.se_apotekensservice.or._1.ForskrivarinfoResponse;
import riv.se_apotekensservice.or._1.OrdinationslistaResponse;
import se.riv.inera.se.apotekensservice.argos.v1.ArgosHeaderType;
import se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerRequestType;
import se.riv.se.apotekensservice.or.hamtaaktuellaordinationerresponder.v1.HamtaAktuellaOrdinationerResponseType;

@WebService(serviceName = "HamtaAktuellaOrdinationerResponderService", endpointInterface = "se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.HamtaAktuellaOrdinationerResponderInterface", portName = "HamtaAktuellaOrdinationerResponderPort", targetNamespace = "urn:riv:inera:se.apotekensservice:or:HamtaAktuellaOrdinationer:1:rivtabp20", wsdlLocation = "schemas/interactions/HamtaAktuellaOrdinationerInteraction/HamtaAktuellaOrdinationerInteraction_1.0_rivtabp20.wsdl")
public class HamtaAllaAktuellaOrdinationerTestProducer implements HamtaAktuellaOrdinationerResponderInterface {

    @Override
    public HamtaAktuellaOrdinationerResponseType hamtaAktuellaOrdinationer(
	    HamtaAktuellaOrdinationerRequestType parameters, org.w3c.addressing.v1.AttributedURIType logicalAddress,
	    ArgosHeaderType argosHeader)
	    throws se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.ApplicationException,
	    se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.SystemException {

	String personnummer = parameters.getPersonnummer();

	if (personnummer == null || "".equals(personnummer)) {
	    throw new RuntimeException("Personnummer is mandatory!");
	}

	if ("APPLICATIONEXCEPTION".equals(personnummer)) {
	    throw new se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.ApplicationException(
		    "APPLICATIONEXCEPTION");
	}

	if ("SYSTEMEXCEPTION".equals(personnummer)) {
	    throw new se.riv.inera.se.apotekensservice.or.hamtaaktuellaordinationer.v1.rivtabp20.SystemException(
		    "SYSTEMEXCEPTION");
	}

	HamtaAktuellaOrdinationerResponseType response = new HamtaAktuellaOrdinationerResponseType();
	response.getApoteksInformationLista().add(createApoteksInformation());
	response.getArbetsplatsInfoLista().add(createArbetsplatsInfo());
	response.getArtikelLista().add(createArtikelResponse());
	response.getForskrivarInfoLista().add(createForskrivarinfo());
	response.setOrdinationslista(createOrdinationsLIsta(personnummer));
	return response;
    }

    private ApoteksinformationResponse createApoteksInformation() {
	ApoteksinformationResponse apoteksInformation = new ApoteksinformationResponse();
	apoteksInformation.setAktorsnamn("Aktornamn");
	apoteksInformation.setAktorsorgnr(1L);
	apoteksInformation.setAllmantelefon("Allmantelefon");
	apoteksInformation.setBesoksadress("Besoksadress");
	apoteksInformation.setEReceptDjur(true);
	apoteksInformation.setFax("Fax");
	apoteksInformation.setGlnKod("GlnKod");
	apoteksInformation.setHuvudtypkod("Huvudtypkod");
	apoteksInformation.setNamn("Namne");
	apoteksInformation.setOrt("Ort");
	apoteksInformation.setRegistrerad(true);
	// apoteksInformation.setSlutdatum();
	// apoteksInformation.setStartdatum(value);
	return apoteksInformation;
    }

    private ArbetsplatsInfoResponse createArbetsplatsInfo() {
	ArbetsplatsInfoResponse arbetsplatsInfo = new ArbetsplatsInfoResponse();
	arbetsplatsInfo.setArbetsplatskod("Arbetsplatskod");
	arbetsplatsInfo.setArbetsplatsNamn("Arbetsplatsnamn");
	arbetsplatsInfo.setArbetsplatsOrt("Arbetsplatsort");
	arbetsplatsInfo.setFaxnummer("Faxnummer");
	// arbetsplatsInfo.setGiltigSlut(value);
	// arbetsplatsInfo.setGiltigStart(value);
	arbetsplatsInfo.setPostadress("Potadress");
	arbetsplatsInfo.setPostnummer("Postnummer");
	arbetsplatsInfo.setRegistrerad(true);
	arbetsplatsInfo.setTelefonnummer1("Telefonnummer1");
	arbetsplatsInfo.setTelefonnummer2("Telefonnummer2");
	return arbetsplatsInfo;
    }

    private ArtikelResponse createArtikelResponse() {
	ArtikelResponse artikel = new ArtikelResponse();
	artikel.setAlternativtAntalKlartext("AlternativtAntalKlartext");
	artikel.setAntalKlartext("AntalKlartext");
	artikel.setArtikelbenamning("Artikelbenamning");
	artikel.setAtcKlartext("AtcKlartext");
	artikel.setAtcKod("AtcKod");
	// artikel.setAvregistreringsdatum(value);
	artikel.setBestallningsstoppad(true);
	artikel.setForegaendePrisperiodensvara(true);
	artikel.setFormansberattigad(true);
	artikel.setForpackningsenhet("Forpackningsenhet");
	artikel.setForpackningsinnehall("Forpackningsinnehall");
	artikel.setForpackningsmangd(10D);
	artikel.setForpackningstyp("Forpackningstyp");
	artikel.setForsaljningsstoppad(true);
	// TODO: Fler attribut
	return artikel;
    }

    private ForskrivarinfoResponse createForskrivarinfo() {
	ForskrivarinfoResponse forskrivarInfo = new ForskrivarinfoResponse();
	forskrivarInfo.setEfternamn("Efternamn");
	forskrivarInfo.setFornamn("Fornamn");
	forskrivarInfo.setForskrivarkod("Forskrivarkod");
	forskrivarInfo.setGiltig(true);
	forskrivarInfo.setGruppforskrivarkodKlartext("Gruppforskrivarkod");
	forskrivarInfo.setInskrankt(true);
	return forskrivarInfo;
    }

    private OrdinationslistaResponse createOrdinationsLIsta(String personnummer) {
	DosunderlagResponse dosunderlag = new DosunderlagResponse();
	dosunderlag.setDosproducent("Dosproducent");
	dosunderlag.setDosunderlagsversion(1);

	OrdinationslistaResponse ordinationslista = new OrdinationslistaResponse();
	ordinationslista.setPersonnummer(personnummer);
	ordinationslista.setUnderlagsversion(1);
	ordinationslista.setDosunderlag(dosunderlag);
	return ordinationslista;
    }

}
