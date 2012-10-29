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

package se.skl.tp.crm.carelisting.getlisting.producer;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.skl.riv.crm.carelisting.v1.Facility;
import se.skl.riv.crm.carelisting.v1.Listing;
import se.skl.riv.crm.carelisting.v1.Resource;
import se.skl.riv.crm.carelisting.v1.SubjectOfCare;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderInterface;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.PersonNotFound;
import se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.TechnicalException;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;
import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingResponseType;



@WebService(serviceName = "GetListingResponderService", 
		endpointInterface = "se.skl.riv.crm.carelisting.v1.rivtabp20.getlisting.GetListingResponderInterface", 
		portName = "GetListingResponderPort", 
		targetNamespace = "urn:riv:crm:carelisting:GetListing:1:rivtabp20", 
		wsdlLocation = "schemas/GetListingInteraction_1.0_rivtabp20.wsdl")
public class GetListingImpl implements GetListingResponderInterface {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	public GetListingResponseType getListing(AttributedURIType logicalAddress,
			GetListingRequestType parameters)  throws PersonNotFound,
			TechnicalException {
		
		log.info("getListing({}, {})", logicalAddress, parameters);
		log.info("PersonId received: {})", parameters.getPersonId());

		GetListingResponseType response = new GetListingResponseType();

		if (parameters.getPersonId().equalsIgnoreCase("20011231-2384")) {
			Listing listing1 = new Listing();
			Facility facility1 = new Facility();
			facility1.setFacilityId("SE2321000016-1hz6");
			facility1.setFacilityName("Testvardcentral A");
			facility1.setHasQueue(false);
			facility1.getSupportedListingTypes().add("HLM");
			listing1.setHealthcareFacility(facility1);
			listing1.setListingType("HLM");
			Resource resource1 = new Resource();
			resource1.setResourceId("Resource1Id");
			resource1.setResourceName("Resource1Name");
			listing1.setResource(resource1);
			try {
				GregorianCalendar fromDate = new GregorianCalendar();
				fromDate.setTime(new Date());
				listing1.setValidFromDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(fromDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			try {
				GregorianCalendar tomDate = new GregorianCalendar();
				tomDate.setTime(new Date());
				listing1.setValidToDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(tomDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			
			Listing listing2 = new Listing();
			Facility facility2 = new Facility();
			facility2.setFacilityId("SE2321000016-1hz5");
			facility2.setFacilityName("Testspecialist C");
			facility2.setHasQueue(false);
			facility2.getSupportedListingTypes().add("BVC");
			listing2.setHealthcareFacility(facility1);
			listing2.setListingType("BVC");
			Resource resource2 = new Resource();
			resource2.setResourceId("Resource2Id");
			resource2.setResourceName("Resource2Name");
			listing2.setResource(resource2);
			try {
				GregorianCalendar fromDate = new GregorianCalendar();
				fromDate.setTime(new Date());
				listing2.setValidFromDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(fromDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			try {
				GregorianCalendar tomDate = new GregorianCalendar();
				tomDate.setTime(new Date());
				listing2.setValidToDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(tomDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			
			SubjectOfCare subjectOfCare = new SubjectOfCare();
			subjectOfCare.getListing().add(listing1);
			subjectOfCare.getListing().add(listing2);
			subjectOfCare.setPersonId(parameters.getPersonId());
			response.setSubjectOfCare(subjectOfCare);		
		} else if (parameters.getPersonId().equalsIgnoreCase("19121212-1212")) {
			Listing listing1 = new Listing();
			Facility facility1 = new Facility();
			facility1.setFacilityId("SE2321000016-1hz6");
			facility1.setFacilityName("Testvardcentral A");
			facility1.setHasQueue(false);
			facility1.getSupportedListingTypes().add("HLM");
			listing1.setHealthcareFacility(facility1);
			listing1.setListingType("HLM");
			Resource resource1 = new Resource();
			resource1.setResourceId("Resource1Id");
			resource1.setResourceName("Resource1Name");
			listing1.setResource(resource1);
			try {
				GregorianCalendar fromDate = new GregorianCalendar();
				fromDate.setTime(new Date());
				listing1.setValidFromDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(fromDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			try {
				GregorianCalendar tomDate = new GregorianCalendar();
				tomDate.setTime(new Date());
				listing1.setValidToDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(tomDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			
			SubjectOfCare subjectOfCare = new SubjectOfCare();
			subjectOfCare.getListing().add(listing1);
			subjectOfCare.setPersonId(parameters.getPersonId());
			response.setSubjectOfCare(subjectOfCare);				
		} else if (parameters.getPersonId().equalsIgnoreCase("19301229-9263")) {
			Listing listing1 = new Listing();
			Facility facility1 = new Facility();
			facility1.setFacilityId("SE2321000016-1hz6");
			facility1.setFacilityName("Testvardcentral A");
			facility1.setHasQueue(false);
			facility1.getSupportedListingTypes().add("HLM");
			listing1.setHealthcareFacility(facility1);
			listing1.setListingType("HLM");
			Resource resource1 = new Resource();
			resource1.setResourceId("Resource1Id");
			resource1.setResourceName("Resource1Name");
			listing1.setResource(resource1);
			try {
				GregorianCalendar fromDate = new GregorianCalendar();
				fromDate.setTime(new Date());
				listing1.setValidFromDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(fromDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			try {
				GregorianCalendar tomDate = new GregorianCalendar();
				tomDate.setTime(new Date());
				listing1.setValidToDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(tomDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			
			SubjectOfCare subjectOfCare = new SubjectOfCare();
			subjectOfCare.getListing().add(listing1);
			subjectOfCare.setPersonId(parameters.getPersonId());
			response.setSubjectOfCare(subjectOfCare);				
		} else if (parameters.getPersonId().equalsIgnoreCase("1111")) {
			throw new RuntimeException("Error 3");
		} else {
			Listing listing1 = new Listing();
			Facility facility1 = new Facility();
			facility1.setFacilityId("Facility1Id");
			facility1.setFacilityName("Facility1Name");
			facility1.setHasQueue(false);
			facility1.getSupportedListingTypes().add("XX");
			facility1.getSupportedListingTypes().add("YY");
			listing1.setHealthcareFacility(facility1);
			listing1.setListingType("ListingType1");
			Resource resource1 = new Resource();
			resource1.setResourceId("Resource1Id");
			resource1.setResourceName("Resource1Name");
			listing1.setResource(resource1);
			try {
				GregorianCalendar fromDate = new GregorianCalendar();
				fromDate.setTime(new Date());
				listing1.setValidFromDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(fromDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			try {
				GregorianCalendar tomDate = new GregorianCalendar();
				tomDate.setTime(new Date());
				listing1.setValidToDate(DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(tomDate));
			} catch (DatatypeConfigurationException e) {
				throw new java.lang.Error(e);
			}
			SubjectOfCare subjectOfCare = new SubjectOfCare();
			subjectOfCare.getListing().add(listing1);
			subjectOfCare.setPersonId(parameters.getPersonId());
			response.setSubjectOfCare(subjectOfCare);
		
		}
		return response;
	}

}