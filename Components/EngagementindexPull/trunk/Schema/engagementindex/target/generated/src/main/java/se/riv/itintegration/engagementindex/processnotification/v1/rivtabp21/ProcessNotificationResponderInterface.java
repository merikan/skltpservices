package se.riv.itintegration.engagementindex.processnotification.v1.rivtabp21;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.2
 * Mon Apr 30 19:52:43 CEST 2012
 * Generated source version: 2.2.2
 * 
 */
 
@WebService(targetNamespace = "urn:riv:itintegration:engagementindex:ProcessNotification:1:rivtabp21", name = "ProcessNotificationResponderInterface")
@XmlSeeAlso({se.riv.itintegration.registry.v1.ObjectFactory.class,se.riv.itintegration.engagementindex.processnotificationresponder.v1.ObjectFactory.class,riv.itintegration.engagementindex._1.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ProcessNotificationResponderInterface {

    @WebResult(name = "ProcessNotificationResponse", targetNamespace = "urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1", partName = "parameters")
    @WebMethod(operationName = "ProcessNotification", action = "urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1:ProcessNotification")
    public se.riv.itintegration.engagementindex.processnotificationresponder.v1.ProcessNotificationResponseType processNotification(
        @WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true)
        java.lang.String logicalAddress,
        @WebParam(partName = "parameters", name = "ProcessNotification", targetNamespace = "urn:riv:itintegration:engagementindex:ProcessNotificationResponder:1")
        se.riv.itintegration.engagementindex.processnotificationresponder.v1.ProcessNotificationType parameters
    );
}
