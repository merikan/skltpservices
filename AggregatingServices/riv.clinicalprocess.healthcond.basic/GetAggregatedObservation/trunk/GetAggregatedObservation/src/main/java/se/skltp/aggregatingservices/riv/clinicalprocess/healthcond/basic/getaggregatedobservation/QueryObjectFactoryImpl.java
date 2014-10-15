package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.basic.getaggregatedobservation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Node;

import se.riv.clinicalprocess.healthcond.basic.getobservationresponder.v1.GetObservationType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryImpl implements QueryObjectFactory {

	private static final Logger log = LoggerFactory.getLogger(QueryObjectFactoryImpl.class);
	private static final JaxbUtil ju = new JaxbUtil(GetObservationType.class);

	private String eiServiceDomain;
	public void setEiServiceDomain(String eiServiceDomain) {
		this.eiServiceDomain = eiServiceDomain;
	}

	private String eiCategorization;
	public void setEiCategorization(String eiCategorization) {
		this.eiCategorization = eiCategorization;
	}

	/**
	 * Transformerar GetMeasurement request till EI FindContent request enligt:
	 * 
	 * 1. patientId --> registeredResidentIdentification
	 * 2. riv:clinicalprocess:healthcond:basic --> serviceDomain
	 * 3. chb-go --> categorization
	 * 4. sourceSystemId.extension --> sourceSystem
	 */
	@Override
	public QueryObject createQueryObject(Node node) {
		
		GetObservationType request = (GetObservationType)ju.unmarshal(node);
		
		if (log.isDebugEnabled()) log.debug("Transformed payload for pid: {}", request.getPatientId().getExtension());

		FindContentType fc = new FindContentType();		
		fc.setRegisteredResidentIdentification(request.getPatientId().getExtension());
		fc.setServiceDomain(eiServiceDomain);
		fc.setCategorization(eiCategorization);
		fc.setSourceSystem(getSourceSystem(request));
		
		QueryObject qo = new QueryObject(fc, request);

		return qo;
	}

	String getSourceSystem(GetObservationType request) {
		if(request.getSourceSystemId() == null || StringUtils.isBlank(request.getSourceSystemId().getExtension())){
			return null;
		}
		return request.getSourceSystemId().getExtension();
	}
}
