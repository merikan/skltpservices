Denna information är enbart relevant under felsökningsfasen av Hamtameddelande relaterat till https://skl-tp.atlassian.net/browse/SKLTP-121


Följande steg behöver göras för att kunna upprepa problem med until-successful eller liknande i druglogistics-dosedispensing-HamtaMeddelanden-virtualisering.


1. testproducer 
Ändra urlen i Test-producer/src/main/java/se/riv/druglogistics/dosdispensing/hamtameddelanden/v1/Producer.java
så att den inte använder https.
Dvs, det ska se ut någon liknande detta:
	private static final String URL = "http://localhost:10000/teststub/HamtaMeddelanden/1/rivtabp20";

	
2. vp-services 
För att få upp en muleserver mot HamtaMeddelande behöver följande inställningar göras:
(Filen i fråga: vp-services/src/test/java/se/skl/tp/vp/VpMuleServer.java).

Ändringar:
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
Filen HamtaMeddelande-soapui-project.xml innehåller ett enkelt SoapUI-test för hämtameddelande. Just nu är
den konfigurerad med glnkoden 3333333333 som skapar ett timeout-fel. Glnkod 7313276012909 ska resultera i ett
normalt svar.


