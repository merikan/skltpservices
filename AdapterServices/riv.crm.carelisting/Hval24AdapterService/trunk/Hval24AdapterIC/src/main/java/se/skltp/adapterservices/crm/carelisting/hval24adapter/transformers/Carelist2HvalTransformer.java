package se.skltp.adapterservices.crm.carelisting.hval24adapter.transformers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;
import se.skl.tp.vp.util.helper.PayloadHelper;

public class Carelist2HvalTransformer extends AbstractMessageAwareTransformer
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public Carelist2HvalTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class);
        
    }
    
	@Override
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			// Get receiver to adress from SOAP Header
			PayloadHelper payloadHelper = new PayloadHelper(message);
			String receiverId = payloadHelper.extractReceiverFromPayload();
			
			log.info("ReceiverId extracted from mule message: {} ",receiverId);
			
			/*
			 * Position reader in position to read Body element
			 */
			ReversibleXMLStreamReader reader = (ReversibleXMLStreamReader)message.getPayload();
			payloadHelper.positionBodyInPayload(reader);

			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(GetListingRequestType.class).createUnmarshaller();
            GetListingRequestType request = (GetListingRequestType)((JAXBElement)unmarshaller.unmarshal(reader)).getValue();

			// Extract the request information and build a request string applicable for hval
			String personId = request.getPersonId();
			
			// Remove dash in personnummer
			if (personId != null && personId.length() == 13) {
				personId = personId.substring(0, 8) + personId.substring(9,13);
			}

			// Build string
			String payload = "?arg=" + receiverId + personId;

			if(logger.isDebugEnabled()){
				log.debug("Transformed carelist-getListing-request to HVAL request: {}", payload);
			}
			
			return payload;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}