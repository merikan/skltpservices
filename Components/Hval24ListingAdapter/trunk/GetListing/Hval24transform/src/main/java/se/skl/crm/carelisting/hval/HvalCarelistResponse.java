package se.skl.crm.carelisting.hval;

import java.text.MessageFormat;

public class HvalCarelistResponse {
	private final static int SIZE_LENGTH    		= 4;
	private final static int SIZE_RET_CODE			= 5;
	private final static int SIZE_PERSON_ID 		= 12;
	private final static int SIZE_SURNAME 			= 40;
	private final static int SIZE_LANSKOD 			= 4;
	private final static int SIZE_LASTNAME 			= 40;
	private final static int SIZE_TYPE 				= 2;
	private final static int SIZE_VARDGIVARE_CODE	= 64;
	private final static int SIZE_VARDGIVARE_NAME	= 40;
	private final static int SIZE_DATE				= 10;
	private final static int SIZE_ENHET_CODE		= 64;
	private final static int SIZE_ENHET_NAME		= 40;

	// All positions are 1 less as we start counting from 0 instead of 1
	private final static int POS_1_LENGTH    			= 0;
	private final static int POS_2_RET_CODE  			= 4;
	private final static int POS_3_PERSON_ID 			= 9;
	private final static int POS_4_SURNAME   			= 21;
	private final static int POS_5_LASTNAME 			= 61;
	private final static int POS_6A_LANSKOD				= 274;
	private final static int POS_6_TYPE_GRP1	    	= 335;
	private final static int POS_7_VARDGIVARE_CODE_GRP1 = 337;
	private final static int POS_8_VARDGIVARE_NAME_GRP1	= 401;
	private final static int POS_9_DATE_GRP1			= 441;
	private final static int POS_10_ENHET_CODE_GRP1		= 451;
	private final static int POS_11_ENHET_NAME_GRP1		= 515;

	private final static int POS_12_TYPE_GRP2	    	= 555;
	private final static int POS_13_VARDGIVARE_CODE_GRP2= 557;
	private final static int POS_14_VARDGIVARE_NAME_GRP2= 621;
	private final static int POS_15_DATE_GRP2			= 661;
	private final static int POS_16_ENHET_NAME_GRP2		= 671;
	private final static int POS_17_ENHET_CODE_GRP2		= 711;

	private final static int POS_18_TYPE_GRP3	    	= 775;
	private final static int POS_19_ENHET_CODE_GRP3		= 777;	
	private final static int POS_20_ENHET_NAME_GRP3		= 841;
	private final static int POS_21_DATE_GRP3			= 881;
	
	public String length   	= null;
	public Integer retCode 	= null;
	public String personId 	= null;
	public String fornamn 	= null;
	public String efternamn = null;
	public String lansKod = null;
	public Integer valTypGrp1	= null;
	public String listningVardgivareKodGrp1 = null;
	public String listningVardgivareNamnGrp1 = null;
	public String listningDatumGrp1 = null;
	public String listningEnhetskodGrp1 = null;
	public String listningEnhetsnamnGrp1 = null;

	public Integer valTypGrp2	= null;
	public String listningVardgivareKodGrp2 = null;
	public String listningVardgivareNamnGrp2 = null;
	public String listningDatumGrp2 = null;
	public String listningEnhetskodGrp2 = null;
	public String listningEnhetsnamnGrp2 = null;

	public Integer valTypGrp3	= null;
	public String listningDatumGrp3 = null;
	public String listningEnhetskodGrp3 = null;
	public String listningEnhetsnamnGrp3 = null;

	/**
	 * Extracts the information from the response-string
	 * 
	 * @param payload
	 * @return the extracted response
	 */
	public static HvalCarelistResponse extract(String payload) {
		HvalCarelistResponse response = new HvalCarelistResponse();
		
		response.length   = payload.substring(POS_1_LENGTH,    POS_1_LENGTH + SIZE_LENGTH).trim();
		response.retCode  = getInteger(payload.substring(POS_2_RET_CODE,  POS_2_RET_CODE + SIZE_RET_CODE).trim());
		response.personId = payload.substring(POS_3_PERSON_ID, POS_3_PERSON_ID + SIZE_PERSON_ID).trim();
		response.fornamn  = payload.substring(POS_4_SURNAME,   POS_4_SURNAME + SIZE_SURNAME).trim();
		response.efternamn = payload.substring(POS_5_LASTNAME, POS_5_LASTNAME + SIZE_LASTNAME).trim();
		response.lansKod = payload.substring(POS_6A_LANSKOD, POS_6A_LANSKOD + SIZE_LANSKOD).trim();
		response.valTypGrp1 = getInteger(payload.substring(POS_6_TYPE_GRP1, POS_6_TYPE_GRP1 + SIZE_TYPE).trim());
		response.listningVardgivareKodGrp1 = payload.substring(POS_7_VARDGIVARE_CODE_GRP1, POS_7_VARDGIVARE_CODE_GRP1 + SIZE_VARDGIVARE_CODE).trim();
		response.listningVardgivareNamnGrp1 = payload.substring(POS_8_VARDGIVARE_NAME_GRP1, POS_8_VARDGIVARE_NAME_GRP1 + SIZE_VARDGIVARE_NAME).trim();
		response.listningDatumGrp1 = payload.substring(POS_9_DATE_GRP1, POS_9_DATE_GRP1 + SIZE_DATE).trim();
		response.listningEnhetskodGrp1 = payload.substring(POS_10_ENHET_CODE_GRP1, POS_10_ENHET_CODE_GRP1 + SIZE_ENHET_CODE).trim();
		response.listningEnhetsnamnGrp1 = payload.substring(POS_11_ENHET_NAME_GRP1, POS_11_ENHET_NAME_GRP1 + SIZE_ENHET_NAME).trim();
		
		response.valTypGrp2 = getInteger(payload.substring(POS_12_TYPE_GRP2, POS_12_TYPE_GRP2 + SIZE_TYPE).trim());
		response.listningVardgivareKodGrp2 = payload.substring(POS_13_VARDGIVARE_CODE_GRP2, POS_13_VARDGIVARE_CODE_GRP2 + SIZE_VARDGIVARE_CODE).trim();
		response.listningVardgivareNamnGrp2 = payload.substring(POS_14_VARDGIVARE_NAME_GRP2, POS_14_VARDGIVARE_NAME_GRP2 + SIZE_VARDGIVARE_NAME).trim();
		response.listningDatumGrp2 = payload.substring(POS_15_DATE_GRP2, POS_15_DATE_GRP2 + SIZE_DATE).trim();
		response.listningEnhetskodGrp2 = payload.substring(POS_17_ENHET_CODE_GRP2, POS_17_ENHET_CODE_GRP2 + SIZE_ENHET_CODE).trim();
		response.listningEnhetsnamnGrp2 = payload.substring(POS_16_ENHET_NAME_GRP2, POS_16_ENHET_NAME_GRP2 + SIZE_ENHET_NAME).trim();

		response.valTypGrp3 = getInteger(payload.substring(POS_18_TYPE_GRP3, POS_18_TYPE_GRP3 + SIZE_TYPE).trim());
		response.listningDatumGrp3 = payload.substring(POS_21_DATE_GRP3, POS_21_DATE_GRP3 + SIZE_DATE).trim();
		response.listningEnhetskodGrp3 = payload.substring(POS_19_ENHET_CODE_GRP3, POS_19_ENHET_CODE_GRP3 + SIZE_ENHET_CODE).trim();
		response.listningEnhetsnamnGrp3 = payload.substring(POS_20_ENHET_NAME_GRP3, POS_20_ENHET_NAME_GRP3 + SIZE_ENHET_NAME).trim();
		
		return response;
	}
	
	public String toString() {
		return MessageFormat.format("hval-carelist-response: length={0}, retur code={1}, personId={2}, fornamn={3}, efternamn={4}, valtyp1={5}, vardgivarekod1={6}, vardgivarenamn1={7}, listningsdatum1={8}, enhetskod1={9}, enhetsnamn1={10}, valtyp2={11}, vardgivarekod2={12}, vardgivarenamn2={13}, listningsdatum2={14}, enhetskod2={15}, enhetsnamn2={16}, valtyp3={17}, listningsdatum3={18}, enhetskod3={19}, enhetsnamn3={20}", length, retCode, personId,fornamn, efternamn, valTypGrp1, listningVardgivareKodGrp1, listningVardgivareNamnGrp1, listningDatumGrp1, listningEnhetskodGrp1,listningEnhetsnamnGrp1, valTypGrp2, listningVardgivareKodGrp2, listningVardgivareNamnGrp2, listningDatumGrp2, listningEnhetskodGrp2,listningEnhetsnamnGrp2, valTypGrp3, listningDatumGrp3, listningEnhetskodGrp3,listningEnhetsnamnGrp3);
	}
	
	private static Integer getInteger(String in) throws NumberFormatException {
		Integer out = null;
		if (in != null && in.length() > 0) {
			out = Integer.parseInt(in);
		}
		return out;
	}
}
