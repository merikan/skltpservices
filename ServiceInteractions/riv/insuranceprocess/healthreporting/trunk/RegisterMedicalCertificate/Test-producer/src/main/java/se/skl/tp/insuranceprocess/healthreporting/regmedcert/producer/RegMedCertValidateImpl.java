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
package se.skl.tp.insuranceprocess.healthreporting.regmedcert.producer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v2.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v2.RegisterMedicalCertificateResponseType;
import se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v2.RegisterMedicalCertificateType;
import se.skl.riv.insuranceprocess.healthreporting.v2.AktivitetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.Aktivitetskod;
import se.skl.riv.insuranceprocess.healthreporting.v2.ArbetsformagaNedsattningType;
import se.skl.riv.insuranceprocess.healthreporting.v2.ArbetsuppgiftType;
import se.skl.riv.insuranceprocess.healthreporting.v2.EnhetType;
import se.skl.riv.insuranceprocess.healthreporting.v2.FunktionstillstandType;
import se.skl.riv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.skl.riv.insuranceprocess.healthreporting.v2.LakarutlatandeType;
import se.skl.riv.insuranceprocess.healthreporting.v2.MedicinsktTillstandType;
import se.skl.riv.insuranceprocess.healthreporting.v2.PatientType;
import se.skl.riv.insuranceprocess.healthreporting.v2.Prognosangivelse;
import se.skl.riv.insuranceprocess.healthreporting.v2.ReferensType;
import se.skl.riv.insuranceprocess.healthreporting.v2.Referenstyp;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.skl.riv.insuranceprocess.healthreporting.v2.ResultOfCall;
import se.skl.riv.insuranceprocess.healthreporting.v2.SysselsattningType;
import se.skl.riv.insuranceprocess.healthreporting.v2.TypAvFunktionstillstand;
import se.skl.riv.insuranceprocess.healthreporting.v2.TypAvSysselsattning;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardgivareType;
import se.skl.riv.insuranceprocess.healthreporting.v2.VardkontaktType;
import se.skl.riv.insuranceprocess.healthreporting.v2.Vardkontakttyp;
/**
 * Validation class that will certify a webservice call made for MU7263. We will check mandatory/optional fields and all other declared rules.
 * @author matsek
 *
 */

@WebService(
		serviceName = "RegisterMedicalCertificateResponderService", 
		endpointInterface="se.skl.riv.insuranceprocess.healthreporting.registermedicalcertificate.v2.rivtabp20.RegisterMedicalCertificateResponderInterface", 
		portName = "RegisterMedicalCertificateResponderPort", 
		targetNamespace = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificate:2:rivtabp20",
		wsdlLocation = "schemas/RegisterMedicalCertificateInteraction_2.0_rivtabp20.wsdl")
public class RegMedCertValidateImpl implements RegisterMedicalCertificateResponderInterface {

	public RegisterMedicalCertificateResponseType registerMedicalCertificate(
			AttributedURIType logicalAddress,
			RegisterMedicalCertificateType parameters) {
		
			// List of validation errors
			ArrayList validationErrors = new ArrayList();

			// Create a response and set result of validation            
			RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
			ResultOfCall resCall = new ResultOfCall();
			response.setResult(resCall);

		try {
			// Check that we got any data at all
			if (parameters == null) {
				validationErrors.add("No RegisterMedicalCertificate found in incoming data!");
				throw new Exception();
			}
			
			// Check that we got a lakarutlatande element
			if (parameters.getLakarutlatande() == null) {
				validationErrors.add("No Lakarutlatande element found in incoming request data!");
				throw new Exception();
			}
			
			LakarutlatandeType inLakarutlatande = parameters.getLakarutlatande();
			
			// Check that we got an id - mandatory
			if ( inLakarutlatande.getLakarutlatandeId() == null ||
				 inLakarutlatande.getLakarutlatandeId().length() < 1 ) {
				validationErrors.add("No Lakarutlatande Id found!");				
			}
            String emuId = parameters.getLakarutlatande().getLakarutlatandeId();
            
            // Check skickat datum
            if (inLakarutlatande.getSkickatDatum() == null || !inLakarutlatande.getSkickatDatum().isValid()) {
				validationErrors.add("No or wrong skickatDatum found!");				
            }

            // Check patient information
			if ( inLakarutlatande.getPatient() == null) {
				validationErrors.add("No Patient element found!");	
				throw new Exception();
			}			
			PatientType inPatient = inLakarutlatande.getPatient();
			
            // Check patient id - mandatory
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getExtension() == null ||	
				inPatient.getPersonId().getExtension().length() < 1) {
				validationErrors.add("No Patient Id found!");								
			}
			if (inPatient.getPersonId() == null ||	
				inPatient.getPersonId().getRoot() == null ||	
				(!inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.1") && !inPatient.getPersonId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.3.3"))) {
					validationErrors.add("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3");								
				}
			String inPersonnummer = inPatient.getPersonId().getExtension();

            // Check format on personnummer? samordningsnummer?
            
            // Get namn for patient - mandatory
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
				validationErrors.add("No Patient namn elements found! fullstandigtNamn or (fornamn and efternamn) should be set.");								
			}
			if (!fullstandigtPatientNameFound && fornamnPatientFound && !efternamnPatientFound) {
				validationErrors.add("No Patient efternamn found!");								
			}
			if (!fullstandigtPatientNameFound && !fornamnPatientFound && efternamnPatientFound) {
				validationErrors.add("No Patient fornamn found!");								
			}
								            
            // Check lakar information, enhets information and vardgivar information
			if ( inLakarutlatande.getSkapadAvHosPersonal() == null) {
				validationErrors.add("No SkapadAvHosPersonal element found!");	
				throw new Exception();
			}			
            HosPersonalType hosP = inLakarutlatande.getSkapadAvHosPersonal() ; 
            
            // Check lakar id - mandatory
            if (hosP.getPersonalId() == null || 
            	hosP.getPersonalId().getExtension() == null ||
            	hosP.getPersonalId().getExtension().length() < 1) {
				validationErrors.add("No personal-id found!");	            	
            }
            if (hosP.getPersonalId() == null || 
                hosP.getPersonalId().getRoot() == null ||
                !hosP.getPersonalId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
				validationErrors.add("Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1");								
            }
            
            // Check lakarnamn - mandatory
			boolean fullstandigtLakarNameFound = false;
			boolean fornamnLakarFound = false;
			boolean efternamnLakarFound = false;
			if (hosP.getFullstandigtNamn() != null && hosP.getFullstandigtNamn().length() > 0 ) {
				fullstandigtLakarNameFound = true;
			}
			if (hosP.getFornamn() != null && hosP.getFornamn().length() > 0 ) {
				fornamnLakarFound = true;
			}
			if (hosP.getEfternamn() != null && hosP.getEfternamn().length() > 0 ) {
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

            // Check enhet
            if (hosP.getEnhet() == null) {
				validationErrors.add("No enhet found!");	  
				throw new Exception();
            }
            EnhetType enhet = hosP.getEnhet() ;
           
            // Check enhets id - mandatory
            if (enhet.getEnhetsId() == null ||
            	enhet.getEnhetsId().getExtension() == null ||
            	enhet.getEnhetsId().getExtension().length() < 1) {
				validationErrors.add("No enhets-id found!");	            	
            }
            if (enhet.getEnhetsId() == null || 
            	enhet.getEnhetsId().getRoot() == null ||
                !enhet.getEnhetsId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
    			validationErrors.add("Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1");								
            }
            
            // Check enhetsnamn - mandatory
            if (enhet.getEnhetsnamn() == null || 
                enhet.getEnhetsnamn().length() < 1) {
            	validationErrors.add("No enhetsnamn found!");	            	
            }

            // Check enhetsadress - mandatory      
            boolean fullstandigEnhetsAdressFound = false;
			boolean postadressEnhetFound = false;
			boolean postnrEnhetFound = false;
			boolean postortEnhetFound = false;
			if (enhet.getFullstandigAdress() != null && enhet.getFullstandigAdress().length() > 0 ) {
				fullstandigEnhetsAdressFound = true;
			}
			if (enhet.getPostadress() != null && enhet.getPostadress().length() > 0 ) {
				postadressEnhetFound = true;
			}
			if (enhet.getPostnummer() != null && enhet.getPostnummer().length() > 0 ) {
				postnrEnhetFound = true;
			}
			if (enhet.getPostort() != null && enhet.getPostort().length() > 0 ) {
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
			if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && !postnrEnhetFound && !postortEnhetFound) {
				validationErrors.add("No postort found for enhet!");								
			}
			if (!fullstandigEnhetsAdressFound && !postadressEnhetFound && !postnrEnhetFound && postortEnhetFound) {
				validationErrors.add("No postadress and postnummer found for enhet!");								
			}
			if (!fullstandigEnhetsAdressFound && postadressEnhetFound && !postnrEnhetFound && !postortEnhetFound) {
				validationErrors.add("No postnummer and postort found for enhet!");								
			}
			if (!fullstandigEnhetsAdressFound && postadressEnhetFound && !postnrEnhetFound && postortEnhetFound) {
				validationErrors.add("No postadress and postort found for enhet!");								
			}

            // Check vardgivare
            if (enhet.getVardgivare() == null) {
				validationErrors.add("No vardgivare found!");	  
				throw new Exception();
            }
            VardgivareType vardgivare = enhet.getVardgivare();
           
            // Check vardgivare id - mandatory
            if (vardgivare.getVardgivareId() == null ||
            	vardgivare.getVardgivareId().getExtension() == null ||
            	vardgivare.getVardgivareId().getExtension().length() < 1) {
				validationErrors.add("No vardgivare-id found!");	            	
            }
            if (vardgivare.getVardgivareId() == null || 
            	vardgivare.getVardgivareId().getRoot() == null ||
                !vardgivare.getVardgivareId().getRoot().equalsIgnoreCase("1.2.752.129.2.1.4.1")) {
            	validationErrors.add("Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1");								
            }

            // Check vardgivarename - mandatory?!
            if (vardgivare.getVardgivarnamn() == null || 
            	vardgivare.getVardgivarnamn().length() < 1) {
    			validationErrors.add("No vardgivarenamn found!");	            	
            }

            // Fält 1 - no rule
            boolean inSmittskydd = findAktivitetWithCode(parameters.getLakarutlatande().getAktivitet(), Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true:false;

            // Must be set as this element contains a lot of mandatory information
            FunktionstillstandType aktivitetFunktion = findFunktionsTillstandType(inLakarutlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
            if (aktivitetFunktion == null) {
    			validationErrors.add("No funktionstillstand - aktivitet element found!");	
    			throw new Exception();
            }

            // Declared outside as it may be used further down. 
            ReferensType annat = null;

            // Many fields are optional if smittskydd is checked, if not set validate these below
            if (!inSmittskydd) {
                // Fält 2 - Both mandatory
                if (inLakarutlatande.getMedicinsktTillstand() == null) {
        			validationErrors.add("No medicinsktTillstand element found!");	
        			throw new Exception();
                }
                MedicinsktTillstandType medTillstand = inLakarutlatande.getMedicinsktTillstand();
                if (medTillstand.getTillstandskod() == null ||
                	medTillstand.getTillstandskod().getCode() == null ||
                	medTillstand.getTillstandskod().getCode().length() < 1) {
        			validationErrors.add("No tillstandskod in medicinsktTillstand found!");	            	
                }
                if (medTillstand.getTillstandskod() == null || 
                	medTillstand.getTillstandskod().getCodeSystemName() == null ||
                    !medTillstand.getTillstandskod().getCodeSystemName().equalsIgnoreCase("ICD-10")) {
                	validationErrors.add("Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10");								
                }                
                if (medTillstand.getBeskrivning() == null ||
                    medTillstand.getBeskrivning().length() < 1) {
            		validationErrors.add("No beskrivning in medicinsktTillstand found!");	            	
                }
                            
                // Fält 3
                if (inLakarutlatande.getBedomtTillstand() == null) {
        			validationErrors.add("No bedomtTillstand element found!");	
        			throw new Exception();
                }
                if (inLakarutlatande.getBedomtTillstand().getBeskrivning() == null ||
                	inLakarutlatande.getBedomtTillstand().getBeskrivning().length() < 1	) {
        			validationErrors.add("No beskrivning in bedomtTillstand found!");	
        			throw new Exception();
                }
     
                // Fält 4 - vänster     
                FunktionstillstandType inKroppsFunktion = findFunktionsTillstandType(inLakarutlatande.getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
                if (inKroppsFunktion == null ) {
        			validationErrors.add("No funktionstillstand - kroppsfunktion element found!");	
        			throw new Exception();
            	}
                if (inKroppsFunktion.getBeskrivning() == null || 
                	inKroppsFunktion.getBeskrivning().length() < 1) {
        			validationErrors.add("No beskrivning in funktionstillstand - kroppsfunktion found!");	
                }

                // Fält 4 - höger översta kryssrutan
                VardkontaktType inUndersokning = findVardkontaktTyp(inLakarutlatande.getVardkontakt(), Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);     
                
                // Fält 4 - höger näst översta kryssrutan
                VardkontaktType telefonkontakt = findVardkontaktTyp(inLakarutlatande.getVardkontakt(), Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);

                // Fält 4 - höger näst nedersta kryssrutan
                ReferensType journal = findReferensTyp(inLakarutlatande.getReferens(), Referenstyp.JOURNALUPPGIFTER);

                // Fält 4 - höger nedersta kryssrutan
                annat = findReferensTyp(inLakarutlatande.getReferens(), Referenstyp.ANNAT);

                if (inUndersokning==null && telefonkontakt==null && journal==null && annat==null) {
        			validationErrors.add("No vardkontakt or referens element found ! At least one must be set!");
        			throw new Exception();
                }
                if(inUndersokning != null && (inUndersokning.getVardkontaktstid() == null || !inUndersokning.getVardkontaktstid().isValid())) {
        			validationErrors.add("No or wrong date for vardkontakt - min undersokning av patienten found!");                	
                }
                if(telefonkontakt != null && (telefonkontakt.getVardkontaktstid() == null || !telefonkontakt.getVardkontaktstid().isValid())) {
        			validationErrors.add("No or wrong date for vardkontakt - telefonkontakt found!");                	
                }
                if(journal != null && (journal.getDatum() == null || !journal.getDatum().isValid())) {
        			validationErrors.add("No or wrong date for referens - journal found!");                	
                }
                if(annat != null && (annat.getDatum() == null || !annat.getDatum().isValid())) {
        			validationErrors.add("No or wrong date for referens - annat found!");                	
                }
                       
                // Fält 5
                if (aktivitetFunktion.getBeskrivning() == null || 
                	aktivitetFunktion.getBeskrivning().length() < 1) {
        			validationErrors.add("No beskrivning in funktionstillstand - aktivitet found!");	
                }
              
                // Fält 6a - kryssruta 1
                AktivitetType kontaktAF = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);

                // Fält 6a - kryssruta 2
                AktivitetType kontaktFHV = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);

                // Fält 6a - kryssruta 3
                AktivitetType ovrigt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.OVRIGT);

                // Fält 6b - kryssruta 1
                AktivitetType planeradInomSjukvard = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);

                // Fält 6b - kryssruta 2
                AktivitetType planeradAnnat = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);

                // Validera att en är ikryssad
                if (kontaktAF==null && kontaktFHV==null && ovrigt==null && planeradInomSjukvard==null && planeradAnnat==null) {
        			validationErrors.add("No aktivitet element found for field 6a and 6b!Kontakt med AF, Kontakt med FHV, Ovrigt, Planerad eller pagaende behandling inom sjukvard or planerad eller pagaende annan atgard.");	
        			throw new Exception();
                }
                if (ovrigt != null && (ovrigt.getBeskrivning() == null || ovrigt.getBeskrivning().length() < 1)) {
        			validationErrors.add("No beskrivning in aktivitet element Ovrigt found!.");	                	
                }
                if (planeradInomSjukvard != null && (planeradInomSjukvard.getBeskrivning() == null || planeradInomSjukvard.getBeskrivning().length() < 1)) {
        			validationErrors.add("No beskrivning in aktivitet element Planerad eller pagaende behandling inom sjukvard found!.");	                	
                }
                if (planeradAnnat != null && (planeradAnnat.getBeskrivning() == null || planeradAnnat.getBeskrivning().length() < 1)) {
        			validationErrors.add("No beskrivning in aktivitet element planerad eller pagaende annan atgard found!.");	                	
                }
                
                // Fält 7 - validera endast ett val!
                AktivitetType arbRelRehabAktuell = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                AktivitetType arbRelRehabEjAktuell = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
                AktivitetType garEjAttBedommaArbRelRehab = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                if (arbRelRehabAktuell == null && arbRelRehabEjAktuell == null && garEjAttBedommaArbRelRehab == null) {
        			validationErrors.add("No aktivitet element found for field 7.! Arbetslivsrehab aktuell, Arbetslivsrehab ej aktuell eller Arbetslivsrehab gar ej att bedoma.");	
        			throw new Exception();                	
                }
                if ( (arbRelRehabAktuell != null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab != null) ||
                	 (arbRelRehabAktuell != null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab == null) ||
                	 (arbRelRehabAktuell != null && arbRelRehabEjAktuell == null && garEjAttBedommaArbRelRehab != null) ||
                	 (arbRelRehabAktuell == null && arbRelRehabEjAktuell != null && garEjAttBedommaArbRelRehab != null) ) {
        			validationErrors.add("Only one ckeckbox is allowed for field 7! Arbetslivsrehab aktuell, Arbetslivsrehab ej aktuell eller Arbetslivsrehab gar ej att bedoma.");	              	
                }
                
                // Fält 8a - kryssruta 1-3 validera att någon är ikryssad!
                if (aktivitetFunktion.getArbetsformaga() == null || aktivitetFunktion.getArbetsformaga().getSysselsattning().size() == 0) {
        			validationErrors.add("No arbetsformaga element found for field 8a!");	
        			throw new Exception();                	
                }
                if (aktivitetFunktion.getArbetsformaga().getSysselsattning().size() == 0) {
        			validationErrors.add("No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.");	
        			throw new Exception();                	
                }
                
                // Fält 8a - kryssruta 1 validera att beskrivning finns om arbete är ikryssad
                SysselsattningType arbete = findTypAvSysselsattning(aktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.NUVARANDE_ARBETE);
                SysselsattningType arbetslos = findTypAvSysselsattning(aktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.ARBETSLOSHET);
                SysselsattningType foraldraledig = findTypAvSysselsattning(aktivitetFunktion.getArbetsformaga().getSysselsattning(), TypAvSysselsattning.FORALDRALEDIGHET);
                if (arbete==null && arbetslos==null && foraldraledig==null) {
        			validationErrors.add("No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.");	
        			throw new Exception();                	
                }
                
                ArbetsuppgiftType arbetsBeskrivning = aktivitetFunktion.getArbetsformaga().getArbetsuppgift();
                
                if (arbete != null && arbetsBeskrivning == null) {
                	validationErrors.add("No arbetsuppgift element found when arbete set in field 8a!.");	
        			throw new Exception();                	
                }
                if (arbetsBeskrivning.getTypAvArbetsuppgift() == null || arbetsBeskrivning.getTypAvArbetsuppgift().length() < 1) {
                	validationErrors.add("No beskrivning av arbetsuppgift found when arbete set in field 8a!.");	
        			throw new Exception();                	
                }
            }

            // Fält 8b - kryssruta 1
            ArbetsformagaNedsattningType nedsatt14del =  findArbetsformaga(aktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.v2.Nedsattningsgrad.NEDSATT_MED_1_4);

            // Fält 8b - kryssruta 2
            ArbetsformagaNedsattningType nedsatthalften =  findArbetsformaga(aktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.v2.Nedsattningsgrad.NEDSATT_MED_1_2);
            
            // Fält 8b - kryssruta 3
            ArbetsformagaNedsattningType nedsatt34delar =  findArbetsformaga(aktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.v2.Nedsattningsgrad.NEDSATT_MED_3_4);

            // Fält 8b - kryssruta 4
            ArbetsformagaNedsattningType heltNedsatt =  findArbetsformaga(aktivitetFunktion.getArbetsformaga().getArbetsformagaNedsattning(), se.skl.riv.insuranceprocess.healthreporting.v2.Nedsattningsgrad.HELT_NEDSATT);

            if (nedsatt14del == null && nedsatthalften == null && nedsatt34delar == null && heltNedsatt == null) {
            	validationErrors.add("No arbetsformaganedsattning element found 8b!.");	
    			throw new Exception();                	
            }            
            if (nedsatt14del != null && (nedsatt14del.getVaraktighetFrom() == null || !nedsatt14del.getVaraktighetFrom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 1/4 from date found!");		
            }
            if (nedsatt14del != null && (nedsatt14del.getVaraktighetTom() == null || !nedsatt14del.getVaraktighetTom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 1/4 tom date found!");		
            }
            if (nedsatthalften != null && (nedsatthalften.getVaraktighetFrom() == null || !nedsatthalften.getVaraktighetFrom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 1/2 from date found!");		
            }
            if (nedsatthalften != null && (nedsatthalften.getVaraktighetTom() == null || !nedsatthalften.getVaraktighetTom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 1/2 tom date found!");		
            }
            if (nedsatt34delar != null && (nedsatt34delar.getVaraktighetFrom() == null || !nedsatt34delar.getVaraktighetFrom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 3/4 from date found!");		
            }
            if (nedsatt34delar != null && (nedsatt34delar.getVaraktighetTom() == null || !nedsatt34delar.getVaraktighetTom().isValid())) {
            	validationErrors.add("No or wrong date for nedsatt 3/4 tom date found!");		
            }
            if (heltNedsatt != null && (heltNedsatt.getVaraktighetFrom() == null || !heltNedsatt.getVaraktighetFrom().isValid())) {
            	validationErrors.add("No or wrong date for helt nedsatt from date found!");		
            }
            if (heltNedsatt != null && (heltNedsatt.getVaraktighetTom() == null || !heltNedsatt.getVaraktighetTom().isValid())) {
            	validationErrors.add("No or wrong date for helt nedsatt tom date found!");		
            }
            
            // Fält 9 - Motivering - optional
            String motivering = aktivitetFunktion.getArbetsformaga().getMotivering();
            
            // Fält 10 - Prognosangivelse - Validera endast 1 av 4 val giltigt
            boolean arbetsformagaAterstallasHelt = aktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_HELT) == 0;
            boolean arbetsformagaAterstallasDelvis = aktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.ATERSTALLAS_DELVIS) == 0;
            boolean arbetsformagaEjAterstallas = aktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.INTE_ATERSTALLAS) == 0;
            boolean arbetsformagaGarEjAttBedomma = aktivitetFunktion.getArbetsformaga().getPrognosangivelse().compareTo(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA) == 0;
            if (!arbetsformagaAterstallasHelt && !arbetsformagaAterstallasDelvis && !arbetsformagaEjAterstallas && !arbetsformagaGarEjAttBedomma) {
            	validationErrors.add("No prognosangivelse element found 10!.");	
    			throw new Exception();                	            	
            }
            int prognosCount = 0;
            if (arbetsformagaAterstallasHelt) {
            	prognosCount++;
            }
            if (arbetsformagaAterstallasDelvis) {
            	prognosCount++;
            }
            if (arbetsformagaEjAterstallas) {
            	prognosCount++;
            }
            if (arbetsformagaGarEjAttBedomma) {
            	prognosCount++;
            }
            if (prognosCount > 2) {
                validationErrors.add("Only one prognosangivelse should be set! (10)");	
            }            
            
            // Fält 11 - kryssruta 1 och 2 Endast 1 av 2 val möjligt!
            AktivitetType forandratRessatt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
            AktivitetType ejForandratRessatt = findAktivitetWithCode(inLakarutlatande.getAktivitet(), Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
            if (forandratRessatt != null && ejForandratRessatt != null) {
                validationErrors.add("Only one forandrat ressatt could be set! (11)");	
            }
            
            // Fält 12 - kryssruta 1 
            AktivitetType kontaktFKAktuell = findAktivitetWithCode(parameters.getLakarutlatande().getAktivitet(), Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
            
            // Fält 13 - Upplysningar
            // Om fält 4 annat satt eller fält 10 går ej att bedömma satt skall fält 13 innehålla information.
            String kommentar = parameters.getLakarutlatande().getKommentar();
            if ( (annat!=null || arbetsformagaGarEjAttBedomma) && (kommentar == null || kommentar.length() < 1) ){
                validationErrors.add("Upplysningar should contain data as annat(4) or gar ej att bedoma(10) is checked.");	            	
            }

            // Fält 14 - Signeringstidpunkt
            if (inLakarutlatande.getSigneringsdatum() == null || !inLakarutlatande.getSigneringsdatum().isValid()) {
                validationErrors.add("Signeringsdatum must be set (14)");	            	
            }
            
            // Fält 17 - arbetsplatskod
            if (enhet.getArbetsplatskod() == null || 
            	enhet.getArbetsplatskod().getExtension() == null || 
            	enhet.getArbetsplatskod().getExtension().length() < 1) {
                validationErrors.add("Arbetsplatskod for enhet not found!");	            	            	
            }
            if (enhet.getArbetsplatskod() == null || 
                enhet.getArbetsplatskod().getRoot() == null || 
                !enhet.getArbetsplatskod().getRoot().equalsIgnoreCase("1.2.752.29.4.71") ) {
    			validationErrors.add("Wrong o.i.d. for arbetsplatskod! Should be 1.2.752.29.4.71");								
                }
            
			// Check if we got any validation errors that not caused an Exception
			if (validationErrors.size() > 0) {
				throw new Exception();
			} 
			
			// No validation errors! Return OK!            
			resCall.setResultCode(ResultCodeEnum.OK);
			response.setResult(resCall);

			return response;
		} catch (Exception e) {
			resCall.setErrorText(getValidationErrors(validationErrors));
			resCall.setResultCode(ResultCodeEnum.ERROR);
			return response;
		}
	}
	
	private String getValidationErrors(ArrayList validationErrors) {
		int i = 1;
		StringBuffer validationString = new StringBuffer();
		Iterator iterValidationErrors = validationErrors.iterator();
		validationString.append("Validation error " + i++ + ":");
		validationString.append((String)iterValidationErrors.next());
		while (iterValidationErrors.hasNext()) {
			validationString.append("\n\rValidation error " + i++ + ":");
			validationString.append((String)iterValidationErrors.next());
		}
		return validationString.toString();
	}

	private AktivitetType findAktivitetWithCode(List aktiviteter, Aktivitetskod aktivitetskod) {
		AktivitetType foundAktivitet = null;
		if (aktiviteter != null) {
			for (int i = 0; i< aktiviteter.size(); i++) {
				AktivitetType listAktivitet = (AktivitetType)aktiviteter.get(i);
				if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
					foundAktivitet = listAktivitet;
					break;
				}
			}	
		}
		return foundAktivitet;
	}
	
	private FunktionstillstandType findFunktionsTillstandType(List funktionstillstand, TypAvFunktionstillstand funktionstillstandsTyp) {
		FunktionstillstandType foundFunktionstillstand = null;
		if (funktionstillstand != null) {
			for (int i = 0; i< funktionstillstand.size(); i++) {
				FunktionstillstandType listFunktionstillstand = (FunktionstillstandType)funktionstillstand.get(i);
				if (listFunktionstillstand.getTypAvFunktionstillstand() != null && listFunktionstillstand.getTypAvFunktionstillstand().compareTo(funktionstillstandsTyp) == 0) {
					foundFunktionstillstand = listFunktionstillstand;
					break;
				}
			}		
		}
		return foundFunktionstillstand;
	}

	private VardkontaktType findVardkontaktTyp(List vardkontakter, Vardkontakttyp vardkontaktTyp) {
		VardkontaktType foundVardkontaktType = null;
		if (vardkontakter != null) {
			for (int i = 0; i< vardkontakter.size(); i++) {
				VardkontaktType listVardkontakter = (VardkontaktType)vardkontakter.get(i);
				if (listVardkontakter.getVardkontakttyp() != null && listVardkontakter.getVardkontakttyp().compareTo(vardkontaktTyp) == 0) {
					foundVardkontaktType = listVardkontakter;
					break;
				}
			}
		}
		return foundVardkontaktType;
	}
	
	private ReferensType findReferensTyp(List referenser, Referenstyp referensTyp) {
		ReferensType foundReferensType = null;
		if (referenser != null) {
			for (int i = 0; i< referenser.size(); i++) {
				ReferensType listReferenser = (ReferensType)referenser.get(i);
				if (listReferenser.getReferenstyp() != null && listReferenser.getReferenstyp().compareTo(referensTyp) == 0) {
					foundReferensType = listReferenser;
					break;
				}
			}	
		}
		return foundReferensType;
	}	

	private SysselsattningType findTypAvSysselsattning(List sysselsattning, TypAvSysselsattning sysselsattningsTyp) {
		SysselsattningType foundSysselsattningType = null;
		if (sysselsattning != null) {
			for (int i = 0; i< sysselsattning.size(); i++) {
				SysselsattningType listSysselsattning = (SysselsattningType)sysselsattning.get(i);
				if (listSysselsattning.getTypAvSysselsattning() != null && listSysselsattning.getTypAvSysselsattning().compareTo(sysselsattningsTyp) == 0) {
					foundSysselsattningType = listSysselsattning;
					break;
				}
			}
		}
		return foundSysselsattningType;
	}	

	private ArbetsformagaNedsattningType findArbetsformaga(List arbetsformaga, se.skl.riv.insuranceprocess.healthreporting.v2.Nedsattningsgrad arbetsformagaNedsattningTyp) {
		ArbetsformagaNedsattningType foundArbetsformagaType = null;
		if (arbetsformaga != null) {
			for (int i = 0; i< arbetsformaga.size(); i++) {
				ArbetsformagaNedsattningType listArbetsformaga = (ArbetsformagaNedsattningType)arbetsformaga.get(i);
				if (listArbetsformaga.getNedsattningsgrad() != null && listArbetsformaga.getNedsattningsgrad().compareTo(arbetsformagaNedsattningTyp) == 0) {
					foundArbetsformagaType = listArbetsformaga;
					break;
				}
			}	
		}
		return foundArbetsformagaType;
	}	
	
	private XMLGregorianCalendar getDate(String stringDate) throws Exception{
		try {
			GregorianCalendar fromDate = new GregorianCalendar();
			DateFormat dfm = new SimpleDateFormat("yyyyMMdd");
			Date date = dfm.parse(stringDate);
			fromDate.setTime(date);
			return (DatatypeFactory.newInstance().newXMLGregorianCalendar(fromDate));
		} catch (DatatypeConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (ParseException pe) {
			throw new Exception(pe.getMessage());
		}
	}
	
	
}