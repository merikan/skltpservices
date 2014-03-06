package se.skl.crm.carelisting.transformers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.crm.carelisting.hval.HvalCarelistResponse;
import se.skl.riv.crm.carelisting.v1.Facility;
import se.skl.riv.crm.carelisting.v1.Listing;
import se.skl.riv.crm.carelisting.v1.Resource;
import se.skl.riv.crm.carelisting.v1.SubjectOfCare;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;

public class Hval2CarelistTransformer extends AbstractMessageAwareTransformer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public Hval2CarelistTransformer() {
		super();
		registerSourceType(Object.class);
		setReturnClass(Object.class);
	}

	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		return createSoapEnvelope(transformToBodyElement(message, outputEncoding));
	}

	private String transformToBodyElement(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			// Convert response from hval to a string
			String payload = convertStreamToString((InputStream) message.getPayload());

			// Extract the information from the response string
			HvalCarelistResponse hvalResponse = HvalCarelistResponse.extract(payload);

			logger.debug("Transformed data: " + hvalResponse.toString(), hvalResponse.toString());

			// Check if error code > 4. SQL error etc
			if (hvalResponse.retCode == null || hvalResponse.retCode.intValue() > 4) {
				logger.error("Hval2CarelistTransformer: Error code:" + hvalResponse.retCode);
				return createTechnicalException("HVAL24 Error", "Return code " + hvalResponse.retCode.intValue(),
						message);
			}

			// Check that mandatory personnummer was returned
			if (hvalResponse.personId == null || hvalResponse.personId.length() != 12) {
				logger.error("Hval2CarelistTransformer: Error personummer!");
				return createPersonNotFoundException(message);
			}

			// Create a JAXB object that represents the riv-response
			GetListingResponseType response = createResponse(hvalResponse);

			// Transform the JAXB object into a XML payload
			StringWriter writer = new StringWriter();
			Marshaller marshaller = JAXBContext.newInstance(GetListingResponseType.class).createMarshaller();

			/*
			 * Solves the problem that we dont want an extra xml element in
			 * paylaod. <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			 */
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			marshaller.marshal(new JAXBElement(new QName("urn:riv:crm:carelisting:GetListingResponder:1",
					"getListingResponse"), GetListingResponseType.class, response), writer);

			String result = writer.toString();
			logger.debug("Extracted information: {}", result);
			return result;

		} catch (Exception e) {
			logger.error("Hval2CarelistTransformer:" + e.getMessage());
			return createTechnicalException("Transformer Error", "Exception message: " + e.getMessage(), message);
		}
	}

	/*
	 * This is needed to make sure a soap envelope is returned to the client, because the transformer only works on the complete soap envelope
	 * and not only on the body.
	 */
	private String createSoapEnvelope(String result) {
		StringBuffer envelope = new StringBuffer();
		envelope.append("<?xml version='1.0' encoding='UTF-8'?>");
		envelope.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		envelope.append("<soap:Body>");
		envelope.append(result);
		envelope.append("</soap:Body>");
		envelope.append("</soap:Envelope>");
		return envelope.toString();
	}

	private GetListingResponseType createResponse(HvalCarelistResponse hvalResponse) throws Exception {
		// Create return objects, don't set resource as it may not be valid
		// depending on returned data!
		GetListingResponseType response = new GetListingResponseType();
		SubjectOfCare soc = new SubjectOfCare();
		response.setSubjectOfCare(soc);

		// Transform personnummer to have a - inside
		String personNummer = hvalResponse.personId.substring(0, 8) + "-" + hvalResponse.personId.substring(8, 12);

		// Set person id
		soc.setPersonId(personNummer);

		// Extract listinginformation from group 1
		Listing listgroup1 = getGroup1Listing(hvalResponse);
		if (listgroup1 != null) {
			soc.getListing().add(listgroup1);
		}

		// Extract listinginformation from group 3
		Listing listgroup3 = getGroup3Listing(hvalResponse);
		if (listgroup3 != null) {
			soc.getListing().add(listgroup3);
		}

		return response;
	}

	/**
	 * Extract listing information from group 1. If error return null listing
	 * object
	 * 
	 * @return
	 */
	private Listing getGroup1Listing(HvalCarelistResponse hvalResponse) {
		Listing listing = null;

		try {
			// Get listingtype. Will throw exception if no correct is found!
			String listingType = translateTypeGroup1(hvalResponse.valTypGrp1);

			// Check that we have a facility id
			if (hvalResponse.listningEnhetskodGrp1 == null || hvalResponse.listningEnhetskodGrp1.length() == 0) {
				throw new Exception("No listingfacility code!");
			}

			// Check that we have a facility name
			if (hvalResponse.listningEnhetsnamnGrp1 == null || hvalResponse.listningEnhetsnamnGrp1.length() == 0) {
				throw new Exception("No listingfacility name!");
			}

			// Create the listing response
			listing = new Listing();
			listing.setListingType(listingType);
			Facility facility = new Facility();
			listing.setHealthcareFacility(facility);
			facility.setFacilityId(hvalResponse.listningEnhetskodGrp1);
			facility.setFacilityName(hvalResponse.listningEnhetsnamnGrp1);

			if ((hvalResponse.listningVardgivareKodGrp1 != null && hvalResponse.listningVardgivareKodGrp1.length() > 0)
					&& (hvalResponse.listningVardgivareNamnGrp1 != null && hvalResponse.listningVardgivareNamnGrp1
							.length() > 0)) {
				Resource resource = new Resource();
				resource.setResourceId(hvalResponse.listningVardgivareKodGrp1);
				resource.setResourceName(hvalResponse.listningVardgivareNamnGrp1);
				listing.setResource(resource);
			}

			if (getDate(hvalResponse.listningDatumGrp1) != null) {
				listing.setValidFromDate(getDate(hvalResponse.listningDatumGrp1));
			}

		} catch (Exception ex) {
			logger.warn("Hval2CarelistTransformer: Group1 listing error: " + ex.getMessage() + "."
					+ getMetaInformation(hvalResponse));
		}
		return listing;
	}

	private Listing getGroup3Listing(HvalCarelistResponse hvalResponse) {
		Listing listing = null;
		boolean bvcListingFound = false;

		try {
			// Check if we got a listingtype. Needed to log consistent warnings.
			if (hvalResponse.valTypGrp3 != null && hvalResponse.valTypGrp3.intValue() == 4) {
				bvcListingFound = true;
			}

			// Check that we have a facility id
			if (hvalResponse.listningEnhetskodGrp3 == null || hvalResponse.listningEnhetskodGrp3.length() == 0) {
				throw new Exception("No listingfacility code!");
			}

			// Check that we have a facility name
			if (hvalResponse.listningEnhetsnamnGrp3 == null || hvalResponse.listningEnhetsnamnGrp3.length() == 0) {
				throw new Exception("No listingfacility name!");
			}

			// Create the listing response
			listing = new Listing();
			listing.setListingType("BVC");
			Facility facility = new Facility();
			listing.setHealthcareFacility(facility);
			facility.setFacilityId(hvalResponse.listningEnhetskodGrp3);
			facility.setFacilityName(hvalResponse.listningEnhetsnamnGrp3);
			if (getDate(hvalResponse.listningDatumGrp3) != null) {
				listing.setValidFromDate(getDate(hvalResponse.listningDatumGrp3));
			}
		} catch (Exception ex) {
			if (bvcListingFound) {
				logger.warn("Hval2CarelistTransformer: Group3 listing error: " + ex.getMessage() + "."
						+ getMetaInformation(hvalResponse));
			}
		}
		return listing;
	}

	// Translates between HVAL24 format and GetListing format
	private String translateTypeGroup1(Integer hval24Type) throws Exception {
		if (hval24Type == null) {
			throw new Exception("No listing type from HVAL24!");
		} else if (hval24Type.intValue() == 1) {
			return "HLT";
		} else if (hval24Type.intValue() == 2) {
			return "HL";
		} else if (hval24Type.intValue() == 5) {
			return "HLM";
		} else {
			throw new Exception("Wrong listing type from HVAL24");
		}
	}

	private XMLGregorianCalendar getDate(String stringDate) throws Exception {
		if (stringDate == null || stringDate.length() == 0) {
			return null;
		}
		try {
			GregorianCalendar fromDate = new GregorianCalendar();
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dfm.parse(stringDate);
			fromDate.setTime(date);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(fromDate);
		} catch (DatatypeConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (ParseException pe) {
			throw new Exception(pe.getMessage());
		}
	}

	private String getMetaInformation(HvalCarelistResponse hvalResponse) {
		String personId = hvalResponse.personId;
		String lansKod = hvalResponse.lansKod;
		Integer returKod = hvalResponse.retCode;
		return "Returkod=" + returKod + ", Personid=" + personId + ", Lanskod=" + lansKod + ".";
	}

	private String convertStreamToString(InputStream is) throws Exception {
		try {
			StringBuilder sb = new StringBuilder();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	private String createPersonNotFoundException(MuleMessage message) {
		// Create the fault, Soap envelope will be created for us
		StringBuffer result = new StringBuffer();

		result.append("<?xml version='1.0' encoding='UTF-8'?>");
		result.append("<soap:Fault xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		result.append("<faultcode>soap:Server</faultcode>");
		result.append("<faultstring>VP009 Exception:Person with personId not found</faultstring>");
		result.append("<detail>");
		result.append("<PersonNotFound");
		result.append(" xmlns=\"urn:riv:crm:carelisting:GetListingResponder:1\"");
		result.append(" xmlns:a=\"urn:riv:crm:carelisting:1\">");
		result.append("<a:code>Person with personId not found</a:code>");
		result.append("</PersonNotFound>");
		result.append("</detail>");
		result.append("</soap:Fault>");
		return result.toString();
	}

	private String createTechnicalException(String faulDetailCode, String faultDetailDescription, MuleMessage message) {
		// Create the fault, Soap envelope will be created for us
		StringBuffer result = new StringBuffer();

		result.append("<?xml version='1.0' encoding='UTF-8'?>");
		result.append("<soap:Fault xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		result.append("<faultcode>soap:Server</faultcode>");
		result.append("<faultstring>VP009 Exception: TechnicalException</faultstring>");
		result.append("<detail>");
		result.append("<TechnicalException");
		result.append(" xmlns=\"urn:riv:crm:carelisting:GetListingResponder:1\"");
		result.append(" xmlns:a=\"urn:riv:crm:carelisting:1\">");
		result.append("<a:code>" + faulDetailCode + "</a:code>");
		result.append("<a:description>" + faultDetailDescription + "</a:description>");
		result.append("</TechnicalException>");
		result.append("</detail>");
		result.append("</soap:Fault>");

		return result.toString();
	}
}