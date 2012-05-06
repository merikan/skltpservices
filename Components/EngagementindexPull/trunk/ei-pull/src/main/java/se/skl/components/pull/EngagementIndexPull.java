package se.skl.components.pull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    public void doFetchUpdates() {
        String logicalAddress = "Kalle";
        GetLogicalAddresseesByServiceContractType parameters = new GetLogicalAddresseesByServiceContractType();
        ServiceContractNamespaceType serviceContractNameSpace = new ServiceContractNamespaceType();
        serviceContractNameSpace.setServiceContractNamespace("SOME URI");
        parameters.setServiceConsumerHsaId("Some consumer HSA ID");
        parameters.setServiceContractNameSpace(serviceContractNameSpace);
        GetLogicalAddresseesByServiceContractResponseType addressResponse = getAddressesClient.getLogicalAddresseesByServiceContract(logicalAddress, parameters);
        List<String> addressesToContact = addressResponse.getLogicalAddress();
        // List<String> addressesToContact = new ArrayList<String>();
        // addressesToContact.add("Kalle");
        doPull(addressesToContact);
    }

	private void doPull(List<String> addressesToContact) {
        for (String address : addressesToContact) {
            GetUpdatesResponseType updates = pull(address);
            push(address, updates);
        }
	}

	private GetUpdatesResponseType pull(String logicalAddress) {

		GetUpdatesType updateRequest = new GetUpdatesType();
		updateRequest.setServiceDomain("riv:crm:scheduling");
		updateRequest.setTimeStamp(getTimestamp());

		GetUpdatesResponseType response = getUpdatesClient.getUpdates(logicalAddress, updateRequest);
		return response;
	}

	private void push(String logicalAddress, GetUpdatesResponseType updates) {
		UpdateType requestForUpdate = createRequestForUpdate(updates);
		updateClient.update(logicalAddress, requestForUpdate);
	}

	private UpdateType createRequestForUpdate(GetUpdatesResponseType updateResponse) {
		UpdateType requestForUpdate = new UpdateType();
		List<RegisteredResidentEngagementType> updates = updateResponse.getRegisteredResidentEngagement();
		for (RegisteredResidentEngagementType registeredResidentEngagementType : updates) {
			List<EngagementType> engagements = registeredResidentEngagementType.getEngagement();
			for (EngagementType engagementType : engagements) {
				EngagementTransactionType engagementTransaction = new EngagementTransactionType();
				// Var hittar man informtion om vad som skall tas bort?
				// engagementTransaction.setDeleteFlag(value);
				engagementTransaction.setEngagement(engagementType);
				requestForUpdate.getEngagementTransaction().add(engagementTransaction);
			}
		}

		return requestForUpdate;
	}

	String getTimestamp() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationcontext.xml");
		BeanFactory factory = context;
		EngagementIndexPull engagementIndexPull = (EngagementIndexPull) factory.getBean("engagementIndexPull");
		engagementIndexPull.doFetchUpdates();
	}

}
