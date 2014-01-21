package se.riv.population.residentmaster.lookupresidentforfullprofile.v1;

import javax.jws.WebService;

import se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookUpSpecificationType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileType;
import se.riv.population.residentmaster.v1.*;
import se.riv.population.residentmaster.v1.RelationerTYPE.Relation;

import java.util.*;

@WebService(
		serviceName = "LookupResidentForFullProfileResponderService",
		endpointInterface = "se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface",
		portName = "LookupResidentForFullProfileResponderPort",
		targetNamespace = "urn:riv:population:residentmaster:LookupResidentForFullProfile:1:rivtabp21",
		wsdlLocation = "schemas/interactions/LookupResidentForFullProfileInteraction/LookupResidentForFullProfileInteraction_1.1_RIVTABP21.wsdl")
public class LookupResidentForFullProfileImpl implements LookupResidentForFullProfileResponderInterface {

    enum Attribut {PNR, FNAME, ENAME}
    public static final Map<String, List<Map<Attribut, String>>> RELATIONS = new HashMap<String, List<Map<Attribut, String>>>();
    static {
        Map<Attribut, String> ullaRelation_1 = new HashMap<Attribut, String>();
        ullaRelation_1.put(Attribut.PNR, "201111111115");
        ullaRelation_1.put(Attribut.FNAME, "Axel");
        ullaRelation_1.put(Attribut.ENAME, "Alm");

        Map<Attribut, String> ullaAndFridaRelation = new HashMap<Attribut, String>();
        ullaAndFridaRelation.put(Attribut.PNR, "200802020200");
        ullaAndFridaRelation.put(Attribut.FNAME, "Alva");
        ullaAndFridaRelation.put(Attribut.ENAME, "Alm");

        Map<Attribut, String> ullaRelation_3 = new HashMap<Attribut, String>();
        ullaRelation_3.put(Attribut.PNR, "200001010101");
        ullaRelation_3.put(Attribut.FNAME, "Anna");
        ullaRelation_3.put(Attribut.ENAME, "Alm");

        Map<Attribut, String> fridaRelation_1 = new HashMap<Attribut, String>();
        fridaRelation_1.put(Attribut.PNR, "201010101019");
        fridaRelation_1.put(Attribut.FNAME, "Frans");
        fridaRelation_1.put(Attribut.ENAME, "Kranstege");

        RELATIONS.put("198611062384", Arrays.asList(ullaRelation_1, ullaAndFridaRelation, ullaRelation_3));
        RELATIONS.put("197705232382", Arrays.asList(fridaRelation_1, ullaAndFridaRelation));
    }

	@Override
	public LookupResidentForFullProfileResponseType lookupResidentForFullProfile(
			String arg0, LookupResidentForFullProfileType lookupResidentForFullProfile) {
		LookupResidentForFullProfileResponseType response = new LookupResidentForFullProfileResponseType();
        List<ResidentType> resident = response.getResident();

        for (String personId : lookupResidentForFullProfile.getPersonId()) {
            resident.add(createResidentType(personId));
        }

		return response;
	}

	private ResidentType createResidentType(String personId) {
		ResidentType residentType = new ResidentType();
		residentType.setSekretessmarkering(JaNejTYPE.N);
		residentType.setPersonpost(createPersonPost(personId));
		return residentType;
	}

    private PersonpostTYPE createPersonPost(String personId) {
        PersonpostTYPE personpost = new PersonpostTYPE();
        personpost.setPersonId(personId);
        personpost.setHanvisningsPersonNr(personId);
        personpost.setRelationer(createRelationerType(personId));
        return personpost;
    }

    private RelationerTYPE createRelationerType(String personId) {
        RelationerTYPE relationer = new RelationerTYPE();

        for (Map<Attribut, String> attributes : RELATIONS.get(personId)) {
		    Relation r = new Relation();
            r.setRelationstyp(RelationstypTYPE.V);
            NamnTYPE namn = new NamnTYPE();
            namn.setFornamn(attributes.get(Attribut.FNAME));
            namn.setEfternamn(attributes.get(Attribut.ENAME));
            r.setNamn(namn);

            RelationPersonIdTYPE relationPersonId = new RelationPersonIdTYPE();
            relationPersonId.setPersonNr(attributes.get(Attribut.PNR));
            r.setRelationId(relationPersonId);
            relationer.getRelation().add(r);
        }

		return relationer;
	}

}
