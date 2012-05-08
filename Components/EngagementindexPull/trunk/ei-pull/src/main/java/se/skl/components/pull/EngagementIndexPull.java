package se.skl.components.pull;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import riv.itintegration.engagementindex._1.EngagementTransactionType;
import riv.itintegration.engagementindex._1.EngagementType;
import riv.itintegration.engagementindex._1.RegisteredResidentEngagementType;
import se.riv.itintegration.engagementindex.getupdates.v1.rivtabp21.GetUpdatesResponderInterface;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontract.v1.rivtabp21.GetLogicalAddresseesByServiceContractResponderInterface;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractResponseType;
import se.riv.itintegration.registry.getlogicaladdresseesbyservicecontractresponder.v1.GetLogicalAddresseesByServiceContractType;
import se.riv.itintegration.registry.v1.ServiceContractNamespaceType;

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
        doPull(addressesToContact);
    }

	private void doPull(List<String> addressesToContact) {
        if (addressesToContact != null && addressesToContact.isEmpty()) {
            final String sinceTimeStamp = getFormattedPastTime();
            for (String address : addressesToContact) {
                List<String> serviceDomainList = getServiceDomainList();
                for (String serviceDomain : serviceDomainList) {
                    boolean isComplete;
                    do {
                        GetUpdatesResponseType updates = pull(serviceDomain, address, sinceTimeStamp);
                        if (updates != null) {
                            isComplete = updates.isResponseIsComplete();
                            push(address, updates);
                        } else {
                            isComplete = true;
                        }
                    } while (!isComplete);
                }
            }
        } else {
            log.error("The address list is either null or empty. No fetching of updates could be done at this time.");
        }
	}

	private GetUpdatesResponseType pull(String serviceDomain, String logicalAddress, String sinceTimeStamp) {
		GetUpdatesType updateRequest = new GetUpdatesType();
		updateRequest.setServiceDomain(serviceDomain);
		updateRequest.setTimeStamp(sinceTimeStamp);
        try {
		    return getUpdatesClient.getUpdates(logicalAddress, updateRequest);
        } catch (Exception e) {
            log.fatal("Could not aquire updates from " + logicalAddress + ", using service domain: " + updateRequest.getServiceDomain() + ". Reason:\n", e);
        }
        return null;
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

	private void push(String logicalAddress, GetUpdatesResponseType updates) {
		UpdateType requestForUpdate = createRequestForUpdate(updates);
        try {
            updateClient.update(logicalAddress, requestForUpdate);
        } catch (Exception e) {
            log.fatal("Error while trying to update index! " + updates.getRegisteredResidentEngagement().size() + " posts were unable to be pushed to:"  + logicalAddress + ". Reason:\n", e);
        }
	}

	private UpdateType createRequestForUpdate(GetUpdatesResponseType updateResponse) {
		UpdateType requestForUpdate = new UpdateType();
		for (RegisteredResidentEngagementType registeredResidentEngagementType : updateResponse.getRegisteredResidentEngagement()) {
			for (EngagementType engagementType : registeredResidentEngagementType.getEngagement()) {
				EngagementTransactionType engagementTransaction = new EngagementTransactionType();
				// Var hittar man informtion om vad som skall tas bort?
				// engagementTransaction.setDeleteFlag(value);
				engagementTransaction.setEngagement(engagementType);
				requestForUpdate.getEngagementTransaction().add(engagementTransaction);
			}
		}

		return requestForUpdate;
	}

	private String getFormattedPastTime() {
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
