package se.skltp.aggregatingservices.riv.crm.requeststatus.getaggregatedrequestactivities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Node;

import se.riv.crm.requeststatus.getrequestactivitiesresponder.v1.GetRequestActivitiesType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryImpl implements QueryObjectFactory {

	private static final Logger log = LoggerFactory.getLogger(QueryObjectFactoryImpl.class);
	private static final JaxbUtil ju = new JaxbUtil(GetRequestActivitiesType.class);

	private String eiServiceDomain;
	public void setEiServiceDomain(String eiServiceDomain) {
		this.eiServiceDomain = eiServiceDomain;
	}

	@Override
	public QueryObject createQueryObject(Node node) {
		
		GetRequestActivitiesType reqIn = (GetRequestActivitiesType)ju.unmarshal(node);
		
		String subjectofCareId = reqIn.getSubjectOfCareId();
		
		// TODO: Fyll på med alla query-paramtrar
		log.debug("Transformed payload: pid: {}", subjectofCareId);

		FindContentType fc = new FindContentType();
		fc.setRegisteredResidentIdentification(subjectofCareId);
		fc.setServiceDomain(eiServiceDomain);
		QueryObject qo = new QueryObject(fc, reqIn);

		return qo;
	}
}
