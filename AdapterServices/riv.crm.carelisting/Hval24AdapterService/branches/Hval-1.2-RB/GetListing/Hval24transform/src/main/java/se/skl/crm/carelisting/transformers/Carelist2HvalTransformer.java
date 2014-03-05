package se.skl.crm.carelisting.transformers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;

public class Carelist2HvalTransformer extends AbstractMessageAwareTransformer
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public Carelist2HvalTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class);
        
    }
    
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			// Get receiver to adress from SOAP Header
			String receiverId = (String)message.getProperty("receiverid");
			
			log.info("ReceiverId extracted from mule message: {} ",receiverId);
			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(GetListingRequestType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            GetListingRequestType request = (GetListingRequestType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();

			// Extract the request information and build a request string applicable for hval
			String personId = request.getPersonId();
			
			// Remove dash in personnummer
			if (personId != null && personId.length() == 13) {
				personId = personId.substring(0, 8) + personId.substring(9,13);
			}

			// Build string
			String payload = "?arg=" + receiverId + personId;

			log.debug("Transformed carelist-getListing-request to HVAL request: {}", payload);
			
			return payload;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}