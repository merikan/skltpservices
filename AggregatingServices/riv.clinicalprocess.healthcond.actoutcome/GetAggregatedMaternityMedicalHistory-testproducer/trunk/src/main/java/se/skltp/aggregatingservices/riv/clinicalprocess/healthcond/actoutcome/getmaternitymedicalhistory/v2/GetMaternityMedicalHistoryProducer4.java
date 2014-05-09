package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import javax.jws.WebService;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;


@WebService
public class GetMaternityMedicalHistoryProducer4 implements GetMaternityMedicalHistoryResponderInterface {

	@Override
	public GetMaternityMedicalHistoryResponseType getMaternityMedicalHistory(String logicalAddress,
			GetMaternityMedicalHistoryType parameters) {
		
		System.out.println("Throwing runtimeexception to simulate that the producer 4 can not respond properly!");
		throw new RuntimeException("This Exception is just the symptom of Producer4 simulating its inability to respond");

    }

}
