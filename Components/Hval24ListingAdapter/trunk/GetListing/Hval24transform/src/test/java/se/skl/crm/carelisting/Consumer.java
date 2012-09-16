package se.skl.crm.carelisting;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.Listing;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderService;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.PersonNotFound;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;

public class Consumer {
	
	private GetListingResponderInterface _service = null;

	public static void main(String[] args) throws PersonNotFound, TechnicalException {
		
		ResourceBundle rb     = ResourceBundle.getBundle("hval");
		String serviceAddress = rb.getString("carelisting.url");
    	String personId       = "196210083611";

		if (args.length > 0) {
			serviceAddress = args[0];
		}

		if (args.length > 1) {
			personId = args[1];
		}

		executeTestCall(personId, serviceAddress);
	}

	private static void executeTestCall(String personId, String serviceAddress) throws PersonNotFound, TechnicalException {
		System.out.println("Consumer connecting to "  + serviceAddress);
		Consumer consumer = new Consumer(serviceAddress);
		GetListingResponseType response = consumer.callService(personId);

		long ts = System.currentTimeMillis();
		response = consumer.callService(personId);
		ts = System.currentTimeMillis() - ts;
		printResult(response, ts);
	}

	private static void printResult(GetListingResponseType repsonse, long ts) {		
		System.out.println("Returned ( in " + ts + " ms.): " + 
				"\n PersonId: "      + repsonse.getSubjectOfCare().getPersonId() + 
				"\n Facility-Enhetskod: "      + ((Listing)repsonse.getSubjectOfCare().getListing().get(0)).getHealthcareFacility().getFacilityId() + 
				"\n Listningstyp: "      + ((Listing)repsonse.getSubjectOfCare().getListing().get(0)).getListingType() + 
				"\n Listningsdatum: "      + ((Listing)repsonse.getSubjectOfCare().getListing().get(0)).getValidFromDate() ); 
	}
	
	public Consumer(String serviceAddress) {
		_service = new GetListingResponderService(
			createEndpointUrlFromServiceAddress(serviceAddress)).getGetListingResponderPort();
	}
	
	public GetListingResponseType callService(String personId) throws PersonNotFound, TechnicalException {

		GetListingRequestType request = new GetListingRequestType();
		request.setPersonId(personId);
			
		AttributedURIType logicalAddress = new AttributedURIType();

		GetListingResponseType reply = _service.getListing(logicalAddress, request);

		return reply;
	}

	private URL createEndpointUrlFromServiceAddress(String serviceAddress) {
		try {
			return new URL(serviceAddress + "?wsdl");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed URL Exception: " + e.getMessage());
		}
	}
}