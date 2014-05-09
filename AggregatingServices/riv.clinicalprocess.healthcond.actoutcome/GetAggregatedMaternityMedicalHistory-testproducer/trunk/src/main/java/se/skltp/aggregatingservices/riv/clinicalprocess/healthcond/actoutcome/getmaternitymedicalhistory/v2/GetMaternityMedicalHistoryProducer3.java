package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import javax.jws.WebService;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;

@WebService
public class GetMaternityMedicalHistoryProducer3 implements GetMaternityMedicalHistoryResponderInterface {
	private final static int SLEEP_TIME_MS = 30000;

	@Override
	public GetMaternityMedicalHistoryResponseType getMaternityMedicalHistory(String logicalAddress,
			GetMaternityMedicalHistoryType parameters) {

		// Let it sleep for a while so that the scheduling aggregating service
		// gets mad at us and moves on.
		System.out.println("Producer 3 is sleeping in order to provoke a read time out for aggregated services");
		try {
			Thread.currentThread();
			Thread.sleep(SLEEP_TIME_MS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
