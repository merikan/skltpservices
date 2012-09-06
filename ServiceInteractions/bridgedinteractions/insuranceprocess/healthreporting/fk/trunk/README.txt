Below is a list of all endpoints for the ifv adapter, and sample configuration. Make sure these addresses are loaded
and accessible in runtime. For Tjanstebryggan and Mule 2.2.8 these needs to be set in tb-config.properties.

########################
## Forsakringskassan  ##
########################

######################################
# vard2fk RegisterMedicalCertificate #
######################################
inbound.endpoint.eintyg.taemotlakarintyg.teststub=https://localhost:12000/tb/eintyg/testproducer/TaEmotLakarintyg/1/rivtabp20

inbound.endpoint.eintyg.registermedicalcertificate=http://localhost:11000/tb/eintyg/fk/RegisterMedicalCertificate/3/rivtabp20
outbound.endpoint.eintyg.registermedicalcertificate=${inbound.endpoint.eintyg.taemotlakarintyg.teststub}

inbound.endpoint.eintyg.registermedicalcertificate.teststub.test=https://localhost:12000/tb/eintyg/testproducer/RegisterMedicalCertificate/3/rivtabp20/test
inbound.endpoint.eintyg.registermedicalcertificate.test=http://localhost:11000/tb/eintyg/fk/RegisterMedicalCertificate/3/rivtabp20/test
outbound.endpoint.eintyg.registermedicalcertificate.test=${inbound.endpoint.eintyg.taemotlakarintyg.teststub}

inbound.endpoint.eintyg.registermedicalcertificate.teststub.validate=https://localhost:12000/tb/eintyg/testproducer/RegisterMedicalCertificate/3/rivtabp20/validate
inbound.endpoint.eintyg.registermedicalcertificate.validate=http://localhost:11000/tb/eintyg/fk/RegisterMedicalCertificate/3/rivtabp20/validate
outbound.endpoint.eintyg.registermedicalcertificate.validate=${inbound.endpoint.eintyg.taemotlakarintyg.teststub}

###########################################
# fk2vard ReceiveMedicalCertificateAnswer #
###########################################
inbound.endpoint.eintyg.receivemedicalcertificateanswer.teststub=https://localhost:12000/tb/eintyg/testproducer/ReceiveMedicalCertificateAnswer/1/rivtabp20
inbound.endpoint.eintyg.receivemedicalcertificateanswer=https://localhost:12000/tb/fk/ifv/TaEmotSvar/1/rivtabp20
outbound.endpoint.eintyg.receivemedicalcertificateanswer=${inbound.endpoint.eintyg.receivemedicalcertificateanswer.teststub}

#############################################
# fk2vard ReceiveMedicalCertificateQuestion #
#############################################
inbound.endpoint.eintyg.receivemedicalcertificatequestion.teststub=https://localhost:12000/tb/eintyg/testproducer/ReceiveMedicalCertificateQuestion/1/rivtabp20
inbound.endpoint.eintyg.taemotfraga=https://localhost:12000/tb/fk/ifv/TaEmotFraga/1/rivtabp20
outbound.endpoint.eintyg.receivemedicalcertificatequestion=${inbound.endpoint.eintyg.receivemedicalcertificatequestion.teststub}

inbound.endpoint.eintyg.receivemedicalcertificatequestion.autosvar=http://localhost:11000/tb/eintyg/autosvar/ReceiveMedicalCertificateQuestion/1/rivtabp20

########################################
# vard2fk SendMedicalCertificateAnswer #
########################################
inbound.endpoint.eintyg.taemotsvar.teststub=https://localhost:12000/tb/eintyg/testproducer/TaEmotSvar/1/rivtabp20

inbound.endpoint.eintyg.sendmedicalcertificateanswer=http://localhost:11000/tb/eintyg/fk/SendMedicalCertificateAnswer/3/rivtabp20
outbound.endpoint.eintyg.sendmedicalcertificateanswer=${inbound.endpoint.eintyg.taemotsvar.teststub}

inbound.endpoint.eintyg.sendmedicalcertificateanswer.teststub.test=https://localhost:12000/tb/eintyg/testproducer/SendMedicalCertificateAnswer/3/rivtabp20/test
inbound.endpoint.eintyg.sendmedicalcertificateanswer.teststub.validate=https://localhost:12000/tb/eintyg/testproducer/SendMedicalCertificateAnswer/3/rivtabp20/validate

##########################################
# vard2fk SendMedicalCertificateQuestion #
##########################################
inbound.endpoint.eintyg.taemotfraga.teststub=https://localhost:12000/tb/eintyg/testproducer/TaEmotFraga/1/rivtabp20

inbound.endpoint.eintyg.sendmedicalcertificatequestion=http://localhost:11000/tb/eintyg/fk/SendMedCertQuestion/1/rivtabp20
outbound.endpoint.eintyg.sendmedicalcertificatequestion=${inbound.endpoint.eintyg.taemotfraga.teststub}

inbound.endpoint.eintyg.sendmedicalcertificatequestion.teststub.test=https://localhost:12000/tb/eintyg/testproducer/SendMedicalCertificateQuestion/1/rivtabp20/test
inbound.endpoint.eintyg.sendmedicalcertificatequestion.teststub.validate=https://localhost:12000/tb/eintyg/testproducer/SendMedicalCertificateQuestion/1/rivtabp20/validate