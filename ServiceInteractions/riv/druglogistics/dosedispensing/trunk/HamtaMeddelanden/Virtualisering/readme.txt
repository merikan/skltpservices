Denna information �r enbart relevant under fels�kningsfasen av Hamtameddelande relaterat till https://skl-tp.atlassian.net/browse/SKLTP-121


F�ljande steg beh�ver g�ras f�r att kunna upprepa problem med until-successful eller liknande i druglogistics-dosedispensing-HamtaMeddelanden-virtualisering.


1. testproducer 
�ndra urlen i Test-producer/src/main/java/se/riv/druglogistics/dosdispensing/hamtameddelanden/v1/Producer.java
s� att den inte anv�nder https.
Dvs, det ska se ut n�gon liknande detta:
	private static final String URL = "http://localhost:10000/teststub/HamtaMeddelanden/1/rivtabp20";

	
2. vp-services 
F�r att f� upp en muleserver mot HamtaMeddelande beh�ver f�ljande inst�llningar g�ras:
(Filen i fr�ga: vp-services/src/test/java/se/skl/tp/vp/VpMuleServer.java).

�ndringar:
		SokVagvalsInfoMockInput svimi = new SokVagvalsInfoMockInput();
		List<VagvalMockInputRecord> vagvalInputs = new ArrayList<VagvalMockInputRecord>();
		VagvalMockInputRecord vi = new VagvalMockInputRecord();
		vi.receiverId = "vp-test-producer";
		vi.senderId = "tp";
		vi.rivVersion = "RIVTABP20";
		vi.serviceNamespace = "urn:riv:druglogistics:dosedispensing:HamtaMeddelanden:1:rivtabp20";
		vi.adress = "http://localhost:10000/teststub/HamtaMeddelanden/1/rivtabp20";
		vagvalInputs.add(vi);
		svimi.setVagvalInputs(vagvalInputs);


## SoapUI
Filen HamtaMeddelande-soapui-project.xml inneh�ller ett enkelt SoapUI-test f�r h�mtameddelande. Just nu �r
den konfigurerad med glnkoden 3333333333 som skapar ett timeout-fel. Glnkod 7313276012909 ska resultera i ett
normalt svar.


