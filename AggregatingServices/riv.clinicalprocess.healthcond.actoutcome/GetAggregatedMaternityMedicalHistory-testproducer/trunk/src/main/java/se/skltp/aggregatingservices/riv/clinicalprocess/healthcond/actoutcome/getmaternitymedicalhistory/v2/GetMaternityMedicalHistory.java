package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2;

import java.util.List;

import javax.jws.WebService;

import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistory.v2.rivtabp21.GetMaternityMedicalHistoryResponderInterface;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryResponseType;
import se.riv.clinicalprocess.healthcond.actoutcome.getmaternitymedicalhistoryresponder.v2.GetMaternityMedicalHistoryType;
import se.riv.clinicalprocess.healthcond.actoutcome.v2.MaternityMedicalRecordType;


@WebService
public class GetMaternityMedicalHistory implements GetMaternityMedicalHistoryResponderInterface {

	@Override
	public GetMaternityMedicalHistoryResponseType getMaternityMedicalHistory(String logicalAddress,
			GetMaternityMedicalHistoryType parameters) {
		
		GetMaternityMedicalHistoryBuilder builder = new GetMaternityMedicalHistoryBuilder();
        
        GetMaternityMedicalHistoryResponseType response = new GetMaternityMedicalHistoryResponseType();
        List<MaternityMedicalRecordType> maternityMedicalRecords = response.getMaternityMedicalRecord();

        for (int i = 0; i < 3; i += 1) {
            maternityMedicalRecords.add(builder.createMaternityMedicalRecord(parameters, "Producer1", "20130401020202"));
        }
        return response;
    }

}
