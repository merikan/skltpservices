/**
 * Copyright 2009 Sjukvardsradgivningen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public

 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,

 *   Boston, MA 02111-1307  USA
 */
package se.skl.tp.insuranceprocess.healthreporting.recmedcertquestion.producer;

import iso.v21090.dt.v1.II;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.AktivitetType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Aktivitetskod;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.ArbetsformagaNedsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.ArbetsuppgiftType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.FunktionstillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.MedicinsktTillstandType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Prognosangivelse;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.ReferensType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Referenstyp;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.SysselsattningType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.TypAvFunktionstillstand;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.TypAvSysselsattning;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.VardkontaktType;
import se.skl.riv.insuranceprocess.healthreporting.mu7263.v2.Vardkontakttyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Amnetyp;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.LakarutlatandeEnkelType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.MeddelandeType;
import se.skl.riv.insuranceprocess.healthreporting.qa.v1.Meddelandetyp;
import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionResponseType;
import se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionType;
import se.skl.riv.insuranceprocess.healthreporting.v1.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v1.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v1.OrganisationType;
import se.skl.riv.insuranceprocess.healthreporting.v1.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v1.ResultCodeEnum;
import se.skl.riv.insuranceprocess.healthreporting.v1.ResultOfCall;
import se.skl.riv.insuranceprocess.healthreporting.v1.VardgivareType;

/**
 * Validation class that will certify a webservice call made for a question regarding a medical certificate.. We will check mandatory/optional fields and all other declared rules.
 * @author matsek
 *
 */

@WebService(
		serviceName = "ReceiveMedicalCertificateQuestionResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface", 
		portName = "ReceiveMedicalCertificateQuestionResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1:rivtabp20",
		wsdlLocation = "schemas/ReceiveMedicalCertificateQuestionInteraction_0.9_rivtabp20.wsdl")
public class RecMedCertQuestionValidateImpl implements ReceiveMedicalCertificateQuestionResponderInterface {

	public ReceiveMedicalCertificateQuestionResponseType receiveMedicalCertificateQuestion(
			AttributedURIType logicalAddress,
			ReceiveMedicalCertificateQuestionType parameters) {
		
		// List of validation errors
		ArrayList<String> validationErrors = new ArrayList<String>();

		// Create a response and set result of validation            
		ReceiveMedicalCertificateQuestionResponseType outResponse = new ReceiveMedicalCertificateQuestionResponseType();
		ResultOfCall outResCall = new ResultOfCall();
		outResponse.setResult(outResCall);

		// Validate incoming request
		try {
			// Check that we got any data at all
			if (parameters == null) {
				validationErrors.add("No ReceiveMedicalCertificateQuestion found in incoming data!");
				throw new Exception();
			}
			
			// Check that we got an question element
			if (parameters.getQuestion() == null) {
				validationErrors.add("No Question element found in incoming request data!");
				throw new Exception();
			}
			
			MeddelandeType inQuestion = parameters.getQuestion();
			
			/**
			 *  Check meddelande data + lakarutlatande reference
			 */
			
			// Meddelande id - mandatory
			if ( inQuestion.getMeddelandeId() == null ||
				 inQuestion.getMeddelandeId().length() < 1 ) {
				 validationErrors.add("No Meddelande Id found!");				
			}

			// Ämne - mandatory
			Amnetyp inAmne = inQuestion.getAmne();
			if ( inAmne == null) {
				validationErrors.add("No Amne element found!");				
			}
			
			// Meddelande typ - mandatory
			Meddelandetyp inMeddelandeTyp = inQuestion.getMeddelandetyp();
			if ( inMeddelandeTyp == null) {
				validationErrors.add("No Meddelande type element found!");				
			}
			boolean inFragaFranFK = inMeddelandeTyp.compareTo(Meddelandetyp.FRAGA_FRAN_FK) == 0;
			boolean inFragaFranVarden = inMeddelandeTyp.compareTo(Meddelandetyp.FRAGA_FRAN_VARDEN) == 0;
			if (!inFragaFranFK && !inFragaFranVarden) {
				validationErrors.add("Wrong meddelande type found! Should be fraga fran varden or fraga fran FK.");				
				throw new Exception();
			}
			
			// Komplettering - optional
			if (inQuestion.getKomplettering() != null && inQuestion.getKomplettering().size() > 0) {
				// Check kompletterings data
				
			}
			
			// Avsänt tidpunkt - mandatory
            if (inQuestion.getAvsantTidpunkt() == null || !inQuestion.getAvsantTidpunkt().isValid()) {
				validationErrors.add("No or wrong avsantTidpunkt found!");				
            }
			
			// Sista datum för komplettering - optional
            if (inQuestion.getSistaDatumForKomplettering() != null) {
            	if (!inQuestion.getSistaDatumForKomplettering().isValid()) {
    				validationErrors.add("Wrong sistaDatumForKomplettering found!");				
            	}
            }
			
			// Läkarutlåtande referens - mandatory
            if (inQuestion.getLakarutlatande() == null ) {
				validationErrors.add("No lakarutlatande element found!");	
				throw new Exception();
            }
            LakarutlatandeEnkelType inLakarUtlatande = inQuestion.getLakarutlatande();
            
			// Läkarutlåtande referens - id - mandatory
			if ( inLakarUtlatande.getLakarutlatandeId() == null ||
				inLakarUtlatande.getLakarutlatandeId().length() < 1 ) {
				validationErrors.add("No lakarutlatande-id found!");				
			}

			// Läkarutlåtande referens - avsantTidpunkt - mandatory
            if (inLakarUtlatande.getAvsantTidpunkt() == null || !inLakarUtlatande.getAvsantTidpunkt().isValid()) {
				validationErrors.add("No or wrong lakarutlatande-avsantTidpunkt found!");				
            }

			// Läkarutlåtande referens - patient - mandatory
            if (inLakarUtlatande.getPatient() == null ) {
				validationErrors.add("No lakarutlatande patient element found!");	
				throw new Exception();
            }
            PatientType inPatient = inLakarUtlatande.getPatient();
            
			// Läkarutlåtande referens - patient - personid mandatory
            // Check patient id - mandatory
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getExtension() == null ||	
				inPatient.getPersonId().getExtension().length() < 1) {
				validationErrors.add("No lakarutlatande-Patient Id found!");								
			}
			// Check patient o.i.d.
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getRoot() == null ||	
				(!inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.1") && !inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.3"))) {
					validationErrors.add("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3");								
				}
			String inPersonnummer = inPatient.getPersonId().getExtension();

            // Check format on personnummer? samordningsnummer?
            
			// Läkarutlåtande referens - patient - namn - mandatory
			boolean fullstandigtPatientNameFound = false;
			boolean fornamnPatientFound = false;
			boolean efternamnPatientFound = false;
			if (inPatient.getFullstandigtNamn() != null && inPatient.getFullstandigtNamn().length() > 0 ) {
				fullstandigtPatientNameFound = true;
			}
			if (inPatient.getFornamn() != null && inPatient.getFornamn().length() > 0 ) {
				fornamnPatientFound = true;
			}
			if (inPatient.getEfternamn() != null && inPatient.getEfternamn().length() > 0 ) {
				efternamnPatientFound = true;
			}
			if (!fullstandigtPatientNameFound && !fornamnPatientFound && !efternamnPatientFound) {
				validationErrors.add("No lakarutlatande Patient namn elements found! fullstandigtNamn or (fornamn and efternamn) should be set.");								
			}
			if (!fullstandigtPatientNameFound && fornamnPatientFound && !efternamnPatientFound) {
				validationErrors.add("No lakarutlatande Patient efternamn found!");								
			}
			if (!fullstandigtPatientNameFound && !fornamnPatientFound && efternamnPatientFound) {
				validationErrors.add("No lakarutlatande Patient fornamn found!");								
			}
		
			/**
			 *  Check avsandare data. Depending on direction of question (from varden or FK) validate avsandare.
			 */
			if ( inQuestion.getAvsandare() == null) {
				validationErrors.add("No avsandare element found!");				
				throw new Exception();
			}
			if (inFragaFranVarden) {
				if ( inQuestion.getAvsandare().getHosPersonal() == null) {
					validationErrors.add("No avsandare - hosPersonal element found!");				
					throw new Exception();
				}
				checkHoSPersonal(inQuestion.getAvsandare().getHosPersonal(), validationErrors);
			} else {
				if ( inQuestion.getAvsandare().getOrganisation() == null) {
					validationErrors.add("No avsandare - organisation element found!");				
					throw new Exception();
				}
				checkOrganisation(inQuestion.getAvsandare().getOrganisation(), validationErrors);				
			} 
			
			/**
			 *  Check mottagare data. Depending on direction of question (from varden or FK) validate mottagare.
			 */
			if ( inQuestion.getMottagare() == null) {
				validationErrors.add("No mottagare element found!");				
				throw new Exception();
			}
			if (inFragaFranVarden) {
				if ( inQuestion.getMottagare().getOrganisation() == null) {
					validationErrors.add("No avsandare - organisation element found!");				
					throw new Exception();
				}
				checkOrganisation(inQuestion.getMottagare().getOrganisation(), validationErrors);								
			} else {
				if ( inQuestion.getMottagare().getHosPersonal() == null) {
					validationErrors.add("No avsandare - hosPersonal element found!");				
					throw new Exception();
				}
				checkHoSPersonal(inQuestion.getMottagare().getHosPersonal(), validationErrors);				
			} 
			
			// Check if we got any validation errors that not caused an Exception
			if (validationErrors.size() > 0) {
				throw new Exception();
			} 
			
			// No validation errors! Return OK!            
			outResCall.setResultCode(ResultCodeEnum.OK);
			outResponse.setResult(outResCall);

			return outResponse;
		} catch (Exception e) {
			outResCall.setErrorText(getValidationErrors(validationErrors));
			outResCall.setResultCode(ResultCodeEnum.ERROR);
			return outResponse;
		}
	}
	
	private String getValidationErrors(ArrayList<String> validationErrors) {
		int i = 1;
		StringBuffer validationString = new StringBuffer();
		Iterator<String> iterValidationErrors = validationErrors.iterator();
		validationString.append("Validation error " + i++ + ":");
		validationString.append((String)iterValidationErrors.next());
		while (iterValidationErrors.hasNext()) {
			validationString.append("\n\rValidation error " + i++ + ":");
			validationString.append((String)iterValidationErrors.next());
		}
		return validationString.toString();
	}

	private void checkOrganisation(OrganisationType inOrganisation, List<String> validationErrors) throws Exception {
	}
	
	private void checkHoSPersonal(HosPersonalType inHoSP, List<String> validationErrors) throws Exception {
        
        // Check lakar id - mandatory
        if (inHoSP.getPersonalId() == null || 
        	inHoSP.getPersonalId().getExtension() == null ||
        	inHoSP.getPersonalId().getExtension().length() < 1) {
			validationErrors.add("No personal-id found!");	            	
        }
        // Check lakar id o.i.d.
        if (inHoSP.getPersonalId() == null || 
        	inHoSP.getPersonalId().getRoot() == null ||
            !inHoSP.getPersonalId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
			validationErrors.add("Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1");								
        }
        
        // Check lakarnamn - mandatory
		boolean fullstandigtLakarNameFound = false;
		boolean fornamnLakarFound = false;
		boolean efternamnLakarFound = false;
		if (inHoSP.getFullstandigtNamn() != null && inHoSP.getFullstandigtNamn().length() > 0 ) {
			fullstandigtLakarNameFound = true;
		}
		if (inHoSP.getFornamn() != null && inHoSP.getFornamn().length() > 0 ) {
			fornamnLakarFound = true;
		}
		if (inHoSP.getEfternamn() != null && inHoSP.getEfternamn().length() > 0 ) {
			efternamnLakarFound = true;
		}
		if (!fullstandigtLakarNameFound && !fornamnLakarFound && !efternamnLakarFound) {
			validationErrors.add("No skapadAvHosPersonal namn elements found! fullstandigtNamn or (fornamn and efternamn) should be set.");								
		}
		if (!fullstandigtLakarNameFound && fornamnLakarFound && !efternamnLakarFound) {
			validationErrors.add("No skapadAvHosPersonal efternamn found!");								
		}
		if (!fullstandigtLakarNameFound && !fornamnLakarFound && efternamnLakarFound) {
			validationErrors.add("No skapadAvHosPersonal fornamn found!");								
		}

        // Check that we got a enhet element
        if (inHoSP.getEnhet() == null) {
			validationErrors.add("No enhet element found!");	  
			throw new Exception();
        }
        EnhetType inEnhet = inHoSP.getEnhet() ;
       
        // Check enhets id - mandatory
        if (inEnhet.getEnhetsId() == null ||
        	inEnhet.getEnhetsId().getExtension() == null ||
        	inEnhet.getEnhetsId().getExtension().length() < 1) {
			validationErrors.add("No enhets-id found!");	            	
        }
        // Check enhets o.i.d
        if (inEnhet.getEnhetsId() == null || 
        	inEnhet.getEnhetsId().getRoot() == null ||
            !inEnhet.getEnhetsId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
			validationErrors.add("Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1");								
        }
        
        // Check enhetsnamn - mandatory
        if (inEnhet.getEnhetsnamn() == null || 
        	inEnhet.getEnhetsnamn().length() < 1) {
        	validationErrors.add("No enhetsnamn found!");	            	
        }

        // Check enhetsadress - mandatory      
        boolean fullstandigEnhetsAdressFound = false;
		boolean postadressEnhetFound = false;
		boolean postnrEnhetFound = false;
		boolean postortEnhetFound = false;
		if (inEnhet.getFullstandigAdress() != null && inEnhet.getFullstandigAdress().length() > 0 ) {
			fullstandigEnhetsAdressFound = true;
		}
		if (inEnhet.getPostadress() != null && inEnhet.getPostadress().length() > 0 ) {
			postadressEnhetFound = true;
		}
		if (inEnhet.getPostnummer() != null && inEnhet.getPostnummer().length() > 0 ) {
			postnrEnhetFound = true;
		}
		if (inEnhet.getPostort() != null && inEnhet.getPostort().length() > 0 ) {
			postortEnhetFound = true;
		}
		if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && !postnrEnhetFound && !postortEnhetFound) {
			validationErrors.add("No adress found for enhet! fullstandigtAdress or (postadress and postnummer and postort) should be set.");								
		}
		if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && postnrEnhetFound && postortEnhetFound) {
			validationErrors.add("No postadress found for enhet!");								
		}
		if (!fullstandigEnhetsAdressFound && postadressEnhetFound && !postnrEnhetFound && postortEnhetFound) {
			validationErrors.add("No postnummer found for enhet!");								
		}
		if (!fullstandigEnhetsAdressFound && postadressEnhetFound && postnrEnhetFound && !postortEnhetFound) {
			validationErrors.add("No postort found for enhet!");								
		}
		if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && !postnrEnhetFound && postortEnhetFound) {
			validationErrors.add("No postadress and postnummer found for enhet!");								
		}
		if (!fullstandigEnhetsAdressFound && postadressEnhetFound && !postnrEnhetFound && !postortEnhetFound) {
			validationErrors.add("No postnummer and postort found for enhet!");								
		}
		if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && postnrEnhetFound && !postortEnhetFound) {
			validationErrors.add("No postadress and postort found for enhet!");								
		}

        // Check that we got a vardgivare element
        if (inEnhet.getVardgivare() == null) {
			validationErrors.add("No vardgivare element found!");	  
			throw new Exception();
        }
        VardgivareType inVardgivare = inEnhet.getVardgivare();
       
        // Check vardgivare id - mandatory
        if (inVardgivare.getVardgivareId() == null ||
        	inVardgivare.getVardgivareId().getExtension() == null ||
        	inVardgivare.getVardgivareId().getExtension().length() < 1) {
			validationErrors.add("No vardgivare-id found!");	            	
        }
        // Check vardgivare o.i.d.
        if (inVardgivare.getVardgivareId() == null || 
        	inVardgivare.getVardgivareId().getRoot() == null ||
            !inVardgivare.getVardgivareId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
        	validationErrors.add("Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1");								
        }

        // Check vardgivarename - mandatory
        if (inVardgivare.getVardgivarnamn() == null || 
        	inVardgivare.getVardgivarnamn().length() < 1) {
			validationErrors.add("No vardgivarenamn found!");	            	
        }
	}
}