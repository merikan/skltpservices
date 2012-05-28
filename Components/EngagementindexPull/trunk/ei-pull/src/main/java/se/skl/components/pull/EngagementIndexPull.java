package se.skl.components.pull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import riv.itintegration.engagementindex._1.EngagementTransactionType;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.ResultCodeEnum;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.RegisteredResidentEngagementType;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateResponseType;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;
import se.riv.itintegration.registry.v1.ServiceContractNamespaceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Authors: Hans Thunberg, Henrik Rostam
 */
@Component("engagementIndexPull")
public class EngagementIndexPull {

    @Autowired
    private GetLogicalAddresseesByServiceContractResponderInterface getAddressesClient;

	@Autowired
	private GetUpdatesResponderInterface getUpdatesClient;

	@Autowired
	private UpdateResponderInterface updateClient;

    @Autowired
    private HttpHelper httpHelper;

    private final static Logger log = Logger.getLogger(EngagementIndexPull.class);

    public void doFetchUpdates() {
        log.info("Pull/Push-sequence started.");
    	httpHelper.configHttpConduit(getAddressesClient);
    	httpHelper.configHttpConduit(updateClient);
    	httpHelper.configHttpConduit(getUpdatesClient);

        final String pushServiceContractNamespace = PropertyResolver.get("ei.push.address.servicedomain");
        final String belongsToHsaId = PropertyResolver.get("ei.pull.belongsto.hsaid");
        final String addressServiceAddress = PropertyResolver.get("ei.address.service.address.logical");
        final String pushLogicalAddress = PropertyResolver.get("ei.push.address.logical");
        final String commaSeparatedDomains = PropertyResolver.get("ei.pull.address.servicedomains");
        final String timeOffset = PropertyResolver.get("ei.pull.time.offset");
        final String timestampFormat = PropertyResolver.get("ei.pull.time.format");
        final String updatesSinceTimeStamp = EngagementIndexHelper.getFormattedOffsetTime(DateHelper.now(), timeOffset, timestampFormat);
        final List<String> serviceDomainList = EngagementIndexHelper.stringToList(commaSeparatedDomains);
        final GetLogicalAddresseesByServiceContractType parameters = generateAddressParameters(pushServiceContractNamespace, belongsToHsaId);
        List<String> addressesToContact = new ArrayList<String>();
        try {
            GetLogicalAddresseesByServiceContractResponseType addressResponse = getAddressesClient.getLogicalAddresseesByServiceContract(addressServiceAddress, parameters);
            addressesToContact = addressResponse.getLogicalAddress();
        } catch (Exception e) {
            log.fatal("Could not acquire addresses from " + addressServiceAddress + " which should be contacted for pulling data. Reason:\n", e);
        }
        pushAndPull(addressesToContact, pushLogicalAddress, updatesSinceTimeStamp, serviceDomainList);
        log.info("Pull/Push-sequence ended.");
    }

	private void pushAndPull(List<String> addressesToContact, String pushLogicalAddress, String updatesSinceTimeStamp, List<String> serviceDomainList) {
        if (!addressesToContact.isEmpty()) {
            for (String pullAddress : addressesToContact) {
                for (String serviceDomain : serviceDomainList) {
                    doPushAndPull(serviceDomain, pullAddress, pushLogicalAddress, updatesSinceTimeStamp);
                }
            }
        } else {
            log.error("The address list used to fetch updates is either null or empty. No fetching of updates could be done at this time.");
        }
	}

    private void doPushAndPull(String serviceDomain, String pullAddress, String pushLogicalAddress, String updatesSinceTimeStamp) {
        String lastFetchedRegisteredResidentIdentification = "";
        int amountOfFetchedResults = 0;
        // Continue while there is more data to fetch
        boolean done;
        do {
            GetUpdatesResponseType updates = pull(serviceDomain, updatesSinceTimeStamp, lastFetchedRegisteredResidentIdentification, pullAddress);
            done = pushCycleIsComplete(updates);
            if (updates != null) {
                push(pushLogicalAddress, updates);
                if (!done) {
                    // There are more results to fetch, build list of what we fetched so far, since the producer is stateless.
                    List<RegisteredResidentEngagementType> registeredResidentEngagements = updates.getRegisteredResidentEngagement();
                    int listSize = registeredResidentEngagements.size();
                    int indexOfLastElement = listSize - 1;
                    lastFetchedRegisteredResidentIdentification = registeredResidentEngagements.get(indexOfLastElement).getRegisteredResidentIdentification();
                    amountOfFetchedResults += listSize;
                }
            } else {
                log.error("Received null when pulling data since: " + updatesSinceTimeStamp + ", from address: " + pullAddress + ", using service domain: " + serviceDomain + ".\nPreviously fetched: " + amountOfFetchedResults + " partial results from this address.");
            }
        } while (!done);
    }

    private boolean pushCycleIsComplete(GetUpdatesResponseType updates) {
        return (updates == null) || (updates.isResponseIsComplete());
    }



	private GetUpdatesResponseType pull(String serviceDomain, String updatesSinceTimeStamp, String lastFetchedRegisteredResidentIdentification, String pullAddress) {
		GetUpdatesType updateRequest = new GetUpdatesType();
		updateRequest.setServiceDomain(serviceDomain);
		updateRequest.setTimeStamp(updatesSinceTimeStamp);
        updateRequest.setRegisteredResidentLastFetched(lastFetchedRegisteredResidentIdentification);
        try {
		    return getUpdatesClient.getUpdates(pullAddress, updateRequest);
        } catch (Exception e) {
            log.fatal("Could not aquire updates from " + pullAddress + ", using service domain: " + updateRequest.getServiceDomain() + ". Reason:\n", e);
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
                        log.info("Received " + resultCode.name() + " when updating to " + logicalAddress + ". Result comment: " + updateResponse.getComment() + ".");
                    break;
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
        if (!registeredEngagementTypes.isEmpty()) {
            for (RegisteredResidentEngagementType registeredResidentEngagementType : registeredEngagementTypes) {
                List<EngagementType> engagementTypes = registeredResidentEngagementType.getEngagement();
                if (!engagementTypes.isEmpty()) {
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

    private GetLogicalAddresseesByServiceContractType generateAddressParameters(String pushServiceContractNamespace, String belongsToHsaId) {
        GetLogicalAddresseesByServiceContractType parameters = new GetLogicalAddresseesByServiceContractType();
        ServiceContractNamespaceType serviceContractNameSpace = new ServiceContractNamespaceType();
        serviceContractNameSpace.setServiceContractNamespace(pushServiceContractNamespace);
        parameters.setServiceContractNameSpace(serviceContractNameSpace);
        parameters.setServiceConsumerHsaId(belongsToHsaId);
        return parameters;
    }

}
