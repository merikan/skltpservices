package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedcaredocumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.clinicalprocess.healthcond.description.getcaredocumentationresponder.v2.GetCareDocumentationType;
import se.skltp.agp.cache.CacheHelper;
import se.skltp.agp.service.api.QueryObject;

public class CacheHelperImpl implements CacheHelper {

    private static final Logger log = LoggerFactory.getLogger(CacheHelperImpl.class);
    
    @Override
    public boolean isCachable(QueryObject qo) {
        GetCareDocumentationType o = (GetCareDocumentationType)qo.getExtraArg();
        
        boolean isCachable = (o.getTimePeriod() == null || 
                 (o.getTimePeriod().getEnd() == null &&
                  o.getTimePeriod().getStart() != null)) &&
                o.getCareContactId().size() == 0 &&
                o.getCareUnitHSAid().size() == 0 &&
                o.getSourceSystemHSAid() == null;
                
        log.debug("is cachable? {}", isCachable);
        return isCachable;
    }

}
