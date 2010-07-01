#!/bin/sh
#
# Generate certificates for the SKL TP v 1.0
#
# This script is a modified version of the Apache CXF (2.1.3) example from:
# 
# apache-cxf-2.1.3\samples\wsdl_first_https\bin\gencerts.sh
#
# and the openssl.cnf from:
#
# http://sial.org/howto/openssl/ca/
#

# Initialize the default openssl DataBase.
# According to a default /usr/lib/ssl/openssl.cnf file it is ./demoCA
# Depending on the Openssl version, comment out "crlnumber" in config file.
# We echo 1345 to start the certificate serial number counter.

mkdir -p demoCA/newcerts
cp /dev/null demoCA/index.txt
echo "1346" > demoCA/serial

# Create keypairs and Cert Requests
# This procedure must be done in JKS, because we need to use a JKS keystore.
# The current version of CXF using PCKS12 will not work for a number of 
# internal CXF reasons.

# ### Consumers (Calling clients to TP) ###
#   Riv13606RequestEHRExtractConsumer
    keytool -genkey \
    -alias Riv13606RequestEHRExtractConsumer \
    -dname "CN= Riv13606RequestEHRExtractConsumer, OU=VardgivareA-IVK, O=SKL, ST=GBG, C=SE" \
    -keystore consumer.jks -storetype jks -storepass password -keypass password -keyalg RSA

    keytool -certreq -keystore consumer.jks -storetype jks -storepass password \
    -alias Riv13606RequestEHRExtractConsumer \
    -keypass password -file csrRiv13606RequestEHRExtractConsumer.pem

# ### Producers (Backend national service) ###
#   Riv13606RequestEHRExtractProducer
    keytool -genkey \
    -alias Riv13606RequestEHRExtractProducer \
    -dname "CN=localhost, OU=VardgivareC-IVK, O=SKL, ST=GBG, C=SE" \
    -keystore producer.jks -storetype jks -storepass password -keypass password -keyalg RSA

    keytool -certreq -keystore producer.jks -storetype jks -storepass password \
    -alias Riv13606RequestEHRExtractProducer \
    -keypass password -file csrRiv13606RequestEHRExtractProducer.pem


# Have the CN=TheCA issue a certificate for Consumers and Producers via
# their Certificate Requests.
#  Consumers 
   openssl ca -batch -days 364 -cert cacert.pem -keyfile caprivkey.pem \
   -in csrRiv13606RequestEHRExtractConsumer.pem -out Riv13606RequestEHRExtractConsumer-ca-cert.pem
#  Producers
   openssl ca -batch -days 364 -cert cacert.pem -keyfile caprivkey.pem \
   -in csrRiv13606RequestEHRExtractProducer.pem -out Riv13606RequestEHRExtractProducer-ca-cert.pem

# Rewrite the certificates in PEM only format. This allows us to concatenate
# them into chains.
    openssl x509 -in Riv13606RequestEHRExtractConsumer-ca-cert.pem -out Riv13606RequestEHRExtractConsumer-ca-cert.pem -outform PEM
    openssl x509 -in Riv13606RequestEHRExtractProducer-ca-cert.pem -out Riv13606RequestEHRExtractProducer-ca-cert.pem -outform PEM

# Create a chain readable by CertificateFactory.getCertificates.
    cat Riv13606RequestEHRExtractConsumer-ca-cert.pem cacert.pem > Riv13606RequestEHRExtractConsumer.chain
    cat Riv13606RequestEHRExtractProducer-ca-cert.pem cacert.pem > Riv13606RequestEHRExtractProducer.chain

# Replace the certificates in the tp, client and service keystores with their respective
# full chains.
    keytool -import -file Riv13606RequestEHRExtractConsumer.chain -keystore consumer.jks -storetype jks \
    -alias Riv13606RequestEHRExtractConsumer \
    -storepass password -keypass password -noprompt

    keytool -import -file Riv13606RequestEHRExtractProducer.chain -keystore producer.jks -storetype jks \
    -alias Riv13606RequestEHRExtractProducer \
    -storepass password -keypass password -noprompt

# Create the Truststore file containing the CA cert.
    keytool -import -file cacert.pem -alias TheCA -keystore truststore.jks \
    -storepass password -noprompt
    keytool -import -file cacert.pem -alias TheCA -keystore consumer-truststore.jks -storepass password -noprompt
    keytool -import -file cacert.pem -alias TheCA -keystore producer-truststore.jks -storepass password -noprompt

# List keystores for manual inspection
    keytool -v -list -keystore consumer.jks -storepass password > consumer.jks.log
    keytool -v -list -keystore producer.jks -storepass password > producer.jks.log
    keytool -v -list -keystore truststore.jks -storepass password > truststore.jks.log

