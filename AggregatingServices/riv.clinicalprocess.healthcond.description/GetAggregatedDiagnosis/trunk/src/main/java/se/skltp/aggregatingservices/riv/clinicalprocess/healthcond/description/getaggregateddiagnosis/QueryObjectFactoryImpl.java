package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregateddiagnosis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Node;

import se.riv.clinicalprocess.healthcond.description.getdiagnosisresponder.v2.GetDiagnosisType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryImpl implements QueryObjectFactory {

	private static final Logger log = LoggerFactory.getLogger(QueryObjectFactoryImpl.class);
	private static final JaxbUtil ju = new JaxbUtil(GetDiagnosisType.class);

	private String eiServiceDomain;
	public void setEiServiceDomain(String eiServiceDomain) {
		this.eiServiceDomain = eiServiceDomain;
	}

	@SuppressWarnings("unused")
	private String eiCategorization;
	public void setEiCategorization(String eiCategorization) {
		this.eiCategorization = eiCategorization;
	}

	/**
	 * Transformerar GetDiagnosis request till EI FindContent request enligt:
	 * 
	 * 1. patientId --> registeredResidentIdentification
	 * 2. "riv:clinicalprocess:healthcond:description" --> serviceDomain
	 */
	@Override
	public QueryObject createQueryObject(Node node) {
		
		GetDiagnosisType request = (GetDiagnosisType)ju.unmarshal(node);
		
		if (log.isDebugEnabled()) log.debug("Transformed payload for pid: {}", request.getPatientId().getId());

		FindContentType fc = new FindContentType();		
		fc.setRegisteredResidentIdentification(request.getPatientId().getId());
		fc.setServiceDomain(eiServiceDomain);
		
		QueryObject qo = new QueryObject(fc, request);

		return qo;
	}
}
