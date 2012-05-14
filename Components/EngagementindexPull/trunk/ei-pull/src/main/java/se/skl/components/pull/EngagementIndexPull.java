package se.skl.components.pull;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import riv.itintegration.engagementindex._1.EngagementTransactionType;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.RegisteredResidentEngagementType;
import riv.itintegration.engagementindex._1.ResultCodeEnum;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateResponseType;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;
import se.riv.itintegration.registry.v1.ServiceContractNamespaceType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Authors: Hans Thunberg, Henrik Rostam
 */
@Component
public class EngagementIndexPull {

    @Autowired
    private GetLogicalAddresseesByServiceContractResponderInterface getAddressesClient;

	@Autowired
	private GetUpdatesResponderInterface getUpdatesClient;

	@Autowired
	private UpdateResponderInterface updateClient;

    private final static Logger log = Logger.getLogger(EngagementIndexPull.class);

    public void doFetchUpdates() {
        GetLogicalAddresseesByServiceContractType parameters = new GetLogicalAddresseesByServiceContractType();
        ServiceContractNamespaceType serviceContractNameSpace = new ServiceContractNamespaceType();
        serviceContractNameSpace.setServiceContractNamespace(PropertyResolver.get("ei.push.service.contract.namespace"));
        parameters.setServiceContractNameSpace(serviceContractNameSpace);
        parameters.setServiceConsumerHsaId(PropertyResolver.get("ei.push.service.consumer.hsaid"));
        String logicalAddress = PropertyResolver.get("ei.address.service");
        String pushLogicalAddress = PropertyResolver.get("ei.push.update.destination");
        List<String> addressesToContact = null;
        try {
            GetLogicalAddresseesByServiceContractResponseType addressResponse = getAddressesClient.getLogicalAddresseesByServiceContract(logicalAddress, parameters);
            addressesToContact = addressResponse.getLogicalAddress();
        } catch (Exception e) {
            log.fatal("Could not acquire addresses from " + logicalAddress + " which should be contacted for pulling data. Reason:\n", e);
        }
        pushAndPull(addressesToContact, pushLogicalAddress);
    }

	private void pushAndPull(List<String> addressesToContact, String pushLogicalAddress) {
        if (addressesToContact != null && !addressesToContact.isEmpty()) {
            String commaSeparatedDomains = PropertyResolver.get("ei.push.service.domain.list");
            int offsetFromNowInSeconds = -NumberUtils.toInt(PropertyResolver.get("ei.push.time.offset"));
            Date dateNow = DateHelper.now();
            final String updatesSinceTimeStamp = EngagementIndexHelper.getFormattedOffsetTime(dateNow, offsetFromNowInSeconds, "yyyyMMddHHmmss");
            for (String address : addressesToContact) {
                for (String serviceDomain : EngagementIndexHelper.stringToList(commaSeparatedDomains)) {
                    doPushAndPull(serviceDomain, address, pushLogicalAddress, updatesSinceTimeStamp);
                }
            }
        } else {
            log.error("The address list used to fetch updates is either null or empty. No fetching of updates could be done at this time.");
        }
	}

    private void doPushAndPull(String serviceDomain, String getUpdatesLogicalAddress, String pushLogicalAddress, String updatesSinceTimeStamp) {
        List<String> registeredResidentLastFetched = new LinkedList<String>();
        boolean isNotDone = true;
        // Continue while there is more data to fetch
        while (isNotDone) {
            GetUpdatesResponseType updates = pull(serviceDomain, getUpdatesLogicalAddress, updatesSinceTimeStamp, registeredResidentLastFetched);
            if (updates != null) {
                push(pushLogicalAddress, updates);
                if (updates.isResponseIsComplete()) {
                    // No more results to be fetched
                    isNotDone = false;
                } else {
                    // There are more results to fetch, build list of what we fetched since the producer is stateless.
                    for (RegisteredResidentEngagementType tmpEngagementType : updates.getRegisteredResidentEngagement()) {
                        registeredResidentLastFetched.add(tmpEngagementType.getRegisteredResidentIdentification());
                    }
                }
            } else {
                isNotDone = false;
                log.error("Received null when pulling data since: " + updatesSinceTimeStamp + ", from address: " + getUpdatesLogicalAddress + ", using service domain: " + serviceDomain + ".\nPreviously fetched: " + registeredResidentLastFetched.size() + " partial results from this address.");
            }
        }
    }



	private GetUpdatesResponseType pull(String serviceDomain, String logicalAddress, String sinceTimeStamp, List<String> registeredResidentLastFetched) {
		GetUpdatesType updateRequest = new GetUpdatesType();
		updateRequest.setServiceDomain(serviceDomain);
		updateRequest.setTimeStamp(sinceTimeStamp);
        updateRequest.getRegisteredResidentLastFetched().addAll(registeredResidentLastFetched);
        try {
		    return getUpdatesClient.getUpdates(logicalAddress, updateRequest);
        } catch (Exception e) {
            log.fatal("Could not aquire updates from " + logicalAddress + ", using service domain: " + updateRequest.getServiceDomain() + ". Reason:\n", e);
        }
        return null;
	}

    private void push(String logicalAddress, GetUpdatesResponseType updates) {
        UpdateType requestForUpdate = createRequestForUpdate(updates);
        try {
            UpdateResponseType updateResponse = updateClient.update(logicalAddress, requestForUpdate);
            ResultCodeEnum resultCode = updateResponse.getResultCode();
            if (resultCode != null) { // Switch cases doesn't support null
                switch (resultCode) {
                    case OK:
                    return;
                    case INFO:
                        log.warn("Received unexpected result with code " + resultCode.name() + ". Response comment: " + updateResponse.getComment() + "." + updates.getRegisteredResidentEngagement().size() + " posts was however, successfully pushed to " + logicalAddress + ".");
                    break;
                    case ERROR:
                        log.fatal("Result containing " + updates.getRegisteredResidentEngagement().size() + " posts was pushed to "+ logicalAddress + ", however an error response code was in the reply!\nResult code: " + resultCode.name() + ".\nUpdate response comment:" + updateResponse.getComment());
                    break;
                    default:
                        log.warn("Received unexpected result code '" + resultCode.name() + "' after updating " + updates.getRegisteredResidentEngagement().size() + " posts to "+ logicalAddress + ". Update response comment:" + updateResponse.getComment());
                    break;
                }
            } else {
                log.warn("Received no response (resultCode was null) after updating " + updates.getRegisteredResidentEngagement().size() + " posts to "+ logicalAddress + ". Update response comment:" + updateResponse.getComment());
            }
        } catch (Exception e) {
            log.fatal("Error while trying to update index! " + updates.getRegisteredResidentEngagement().size() + " posts were unable to be pushed to:"  + logicalAddress + ". Reason:\n", e);
        }
    }

	private UpdateType createRequestForUpdate(GetUpdatesResponseType updateResponse) {
		UpdateType requestForUpdate = new UpdateType();
        List<RegisteredResidentEngagementType> registeredEngagementTypes = updateResponse.getRegisteredResidentEngagement();
        if (registeredEngagementTypes != null && !registeredEngagementTypes.isEmpty()) {
            for (RegisteredResidentEngagementType registeredResidentEngagementType : registeredEngagementTypes) {
                List<EngagementType> engagementTypes = registeredResidentEngagementType.getEngagement();
                if (engagementTypes != null && !engagementTypes.isEmpty()) {
                    addTransactionsToUpdateRequest(engagementTypes, requestForUpdate);
                } else {
                    // Engagement list was either null or empty
                    log.debug("Engagement list was either null or empty, no data added to the engagement transaction.");
                }
            }
        } else {
            // RegisteredResidentEngagement list was either null or empty.
            log.debug("Registered resident engagement list was either null or empty, no data added to the engagement transaction.");
        }
		return requestForUpdate;
	}

    private void addTransactionsToUpdateRequest(List<EngagementType> engagementTypes, UpdateType requestForUpdate) {
        for (EngagementType engagementType : engagementTypes) {
            EngagementTransactionType engagementTransaction = new EngagementTransactionType();
            // Var hittar man informtion om vad som skall tas bort?
            // engagementTransaction.setDeleteFlag(value);
            engagementTransaction.setEngagement(engagementType);
            requestForUpdate.getEngagementTransaction().add(engagementTransaction);
        }
    }



	public static void main(String[] args) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationcontext.xml");
		EngagementIndexPull engagementIndexPull = (EngagementIndexPull) factory.getBean("engagementIndexPull");
		engagementIndexPull.doFetchUpdates();
	}

}
