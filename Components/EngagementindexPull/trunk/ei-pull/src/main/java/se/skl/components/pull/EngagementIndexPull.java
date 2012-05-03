package se.skl.components.pull;

import java.text.SimpleDateFormat;
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
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesResponseType;
import se.riv.itintegration.engagementindex.getupdatesresponder.v1.GetUpdatesType;
import se.riv.itintegration.engagementindex.update.v1.rivtabp21.UpdateResponderInterface;
import se.riv.itintegration.engagementindex.updateresponder.v1.UpdateType;

@Component
public class EngagementIndexPull {

	@Autowired
	GetUpdatesResponderInterface getUpdatesClient;

	@Autowired
	UpdateResponderInterface updateClient;

	public void doPull() {
		GetUpdatesResponseType updates = pull("Kalle");
		push("Kalle", updates);
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
		//engagementIndexPull.doPull("LOGICALADDRESS");
	}

}
