package se.skl.components.pull;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Authors: Hans Thunberg, Henrik Rostam
 **/

@Component
public class EngagementIndexPull {

    @Autowired
    private GetLogicalAddresseesByServiceContractResponderInterface getAddressesClient;

	@Autowired
	private GetUpdatesResponderInterface getUpdatesClient;

	@Autowired
	private UpdateResponderInterface updateClient;

    private final static Log log = LogFactory.getLog(EngagementIndexPull.class);

    public void doFetchUpdates() {
        GetLogicalAddresseesByServiceContractType parameters = new GetLogicalAddresseesByServiceContractType();
        ServiceContractNamespaceType serviceContractNameSpace = new ServiceContractNamespaceType();
        serviceContractNameSpace.setServiceContractNamespace(PropertyResolver.get("ei.push.service.contract.namespace"));
        parameters.setServiceContractNameSpace(serviceContractNameSpace);
        parameters.setServiceConsumerHsaId(PropertyResolver.get("ei.push.service.consumer.hsaid"));
        String logicalAddress = PropertyResolver.get("ei.push.address");
        List<String> addressesToContact = null;
        try {
            GetLogicalAddresseesByServiceContractResponseType addressResponse = getAddressesClient.getLogicalAddresseesByServiceContract(logicalAddress, parameters);
            addressesToContact = addressResponse.getLogicalAddress();
        } catch (Exception e) {
            log.fatal("Could not contact " + PropertyResolver.get("ei.push.address.client") + " in order to acquire addresses which should be contacted for pulling data. Reason:\n", e);
        }
        pushAndPull(addressesToContact);
    }

	private void pushAndPull(List<String> addressesToContact) {
        if (addressesToContact != null && !addressesToContact.isEmpty()) {
            final String updatesSinceTimeStamp = getFormattedOffsetTime();
            for (String address : addressesToContact) {
                for (String serviceDomain : getServiceDomainList()) {
                    boolean isComplete = false;
                    List<String> registeredResidentLastFetched = new LinkedList<String>();
                    do {
                        GetUpdatesResponseType updates = pull(serviceDomain, address, updatesSinceTimeStamp, registeredResidentLastFetched);
                        if (updates != null) {
                            push(address, updates);
                            isComplete = updates.isResponseIsComplete();
                        } else {
                            log.error("Received null when pulling data since " + updatesSinceTimeStamp + ", from address: " + address + "\nusing service domain: " + serviceDomain + ". Previously fetched " + registeredResidentLastFetched.size() + " posts from this domain.");
                            isComplete = true;
                        }
                        // If the result is not complete the next request should contain what information which was previously fetched.
                        if (isComplete) {
                            registeredResidentLastFetched.clear();
                        } else {
                            for (RegisteredResidentEngagementType tmpEngagementType : updates.getRegisteredResidentEngagement()) {
                                registeredResidentLastFetched.add(tmpEngagementType.getRegisteredResidentIdentification());
                            }
                        }
                    // Continue while there is more data to fetch
                    } while (!isComplete);
                }
            }
        } else {
            log.error("The address list used to fetch updates is either null or empty. No fetching of updates could be done at this time.");
        }
	}

    private List<String> getServiceDomainList() {
        List<String> serviceDomainList = new LinkedList<String>();
        String commaSeparatedDomains = PropertyResolver.get("ei.push.service.domain.list");
        String[] stringDomainList = commaSeparatedDomains.split(",");
        for (String serviceDomain : stringDomainList) {
            serviceDomainList.add(StringUtils.trim(serviceDomain));
        }
        return serviceDomainList;
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
            switch (resultCode) {
                case OK:
                    return;
                case INFO:
                    // What is supposed to happen here?
                    break;
                case ERROR:
                    log.fatal("Result containing " + updates.getRegisteredResidentEngagement().size() + " posts was pushed to "+ logicalAddress + ", however an error response code was in the reply!\nResult code: " + resultCode.name() + ".\nUpdate response comment:" + updateResponse.getComment());
                    break;
            }
        } catch (Exception e) {
            log.fatal("Error while trying to update index! " + updates.getRegisteredResidentEngagement().size() + " posts were unable to be pushed to:"  + logicalAddress + ". Reason:\n", e);
        }
    }

	private UpdateType createRequestForUpdate(GetUpdatesResponseType updateResponse) {
		UpdateType requestForUpdate = new UpdateType();
        List<RegisteredResidentEngagementType> registeredResidentEngagement = updateResponse.getRegisteredResidentEngagement();
        if (registeredResidentEngagement != null && !registeredResidentEngagement.isEmpty()) {
            for (RegisteredResidentEngagementType registeredResidentEngagementType : registeredResidentEngagement) {
                List<EngagementType> engagement = registeredResidentEngagementType.getEngagement();
                if (engagement != null && !engagement.isEmpty()) {
                    for (EngagementType engagementType : engagement) {
                        EngagementTransactionType engagementTransaction = new EngagementTransactionType();
                        // Var hittar man informtion om vad som skall tas bort?
                        // engagementTransaction.setDeleteFlag(value);
                        engagementTransaction.setEngagement(engagementType);
                        requestForUpdate.getEngagementTransaction().add(engagementTransaction);
                    }
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

	private String getFormattedOffsetTime() {
        int timeOffset = -NumberUtils.toInt(PropertyResolver.get("ei.push.time.offset"));
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(currentDate);
        calendar.set(Calendar.SECOND, timeOffset);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return simpleDateFormat.format(calendar.getTime());
	}

	public static void main(String[] args) {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationcontext.xml");
		EngagementIndexPull engagementIndexPull = (EngagementIndexPull) factory.getBean("engagementIndexPull");
		engagementIndexPull.doFetchUpdates();
	}

}
