package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import javax.jws.WebService;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;


@WebService
public class GetMaternityMedicalHistoryProducer5 implements GetMaternityMedicalHistoryResponderInterface {

	@Override
	public GetMaternityMedicalHistoryResponseType getMaternityMedicalHistory(String logicalAddress,
			GetMaternityMedicalHistoryType parameters) {
		
		System.out.println("Returning NULL from producer5");
		return null;
	}

}
