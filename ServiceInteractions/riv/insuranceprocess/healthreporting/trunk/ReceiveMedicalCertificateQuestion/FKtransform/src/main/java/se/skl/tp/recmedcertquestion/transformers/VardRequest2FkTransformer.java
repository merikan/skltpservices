package se.skl.tp.recmedcertquestion.transformers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerType;

public class VardRequest2FkTransformer extends AbstractMessageAwareTransformer
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public VardRequest2FkTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class); 
    }
    
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		ResourceBundle rb = ResourceBundle.getBundle("fkdata");	    

		try {			
			// Transform the XML payload into a JAXB object
            Unmarshaller unmarshaller = JAXBContext.newInstance(ReceiveMedicalCertificateAnswerType.class).createUnmarshaller();
            XMLStreamReader streamPayload = (XMLStreamReader)((Object[])message.getPayload())[1];
            ReceiveMedicalCertificateAnswerType inRequest = (ReceiveMedicalCertificateAnswerType)((JAXBElement)unmarshaller.unmarshal(streamPayload)).getValue();
		
			// Get receiver to adress from Mule property
			String receiverId = (String)message.getProperty("receiverid");
			

    		AttributedURIType logicalAddressHeader = new AttributedURIType();
    		logicalAddressHeader.setValue(receiverId);

// OBS !! ej null skall vara transformerad request
    		Object[] payloadOut = new Object[] {logicalAddressHeader, null};
            
	        if (logger.isDebugEnabled()) {
	            logger.debug("transformed payload to: " + payloadOut);
	        }

	        return payloadOut;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
		
	private XMLGregorianCalendar getDate(String stringDate) throws Exception{

		try {
			GregorianCalendar fromDate = new GregorianCalendar();
			DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
			Date date = dfm.parse(stringDate);
			fromDate.setTime(date);
			return (DatatypeFactory.newInstance().newXMLGregorianCalendar(fromDate));
		} catch (DatatypeConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (ParseException pe) {
			throw new Exception(pe.getMessage());
		}
	}
	
}