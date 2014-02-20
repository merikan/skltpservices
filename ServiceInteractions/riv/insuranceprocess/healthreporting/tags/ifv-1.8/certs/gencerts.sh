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

# cleanup old runs
rm -rf *.pem exts demoCA *.jks *.chain *.crt *.log

# Initialize the default openssl DataBase.
# According to a default /usr/lib/ssl/openssl.cnf file it is ./demoCA
# Depending on the Openssl version, comment out "crlnumber" in config file.
# We echo 1345 to start the certificate serial number counter.

mkdir -p demoCA/newcerts
cp /dev/null demoCA/index.txt
echo "1345" > demoCA/serial

# This file makes sure that the certificate for CN=TheRA can be a Certificate
# Authority, i.e. can sign the user certificates, e.g. "CN=Wibble".

cat <<EOF > exts
[x509_extensions]
basicConstraints=CA:true
EOF

# Create the CA's keypair and self-signed certificate
#   -x509 means create self-sign cert
#   -keyout means generate keypair
#   -nodes means do not encrypt private key.
#   -set_serial sets the serial number of the certificate

    openssl req -verbose -x509 -new -nodes -set_serial 1234 \
    -subj "/CN=TheCA/OU=NOT FOR PRODUCTION/O=SKL/ST=GBG/C=SE" \
    -days 7300 -out cacert.pem -keyout caprivkey.pem -newkey rsa:1024

# ### Consumers (Calling clients to TP) ###
#   Consumer
    keytool -genkey \
    -alias Consumer \
    -dname "CN=Consumer, OU=VardgivareA, O=SKL, ST=GBG, C=SE" \
    -keystore consumer.jks -storetype jks -storepass password -keypass password -keyalg RSA

    keytool -certreq -keystore consumer.jks -storetype jks -storepass password \
    -alias Consumer \
    -keypass password -file csrConsumer.pem

# ### Producers (Backend national service) ###
#   Producer
    keytool -genkey \
    -alias Producer \
    -dname "CN=producer.riv.se, OU=VardgivareB, O=SKL, ST=GBG, C=SE" \
    -keystore producer.jks -storetype jks -storepass password -keypass password -keyalg RSA

    keytool -certreq -keystore producer.jks -storetype jks -storepass password \
    -alias Producer \
    -keypass password -file csrProducer.pem

#   ProducerLocalhost
    keytool -genkey \
    -alias ProducerLocalhost \
    -dname "CN=localhost, OU=VardgivareC, O=SKL, ST=GBG, C=SE" \
    -keystore producer-localhost.jks -storetype jks -storepass password -keypass password -keyalg RSA

    keytool -certreq -keystore producer-localhost.jks -storetype jks -storepass password \
    -alias ProducerLocalhost \
    -keypass password -file csrProducerLocalhost.pem

# Have the CN=TheCA issue a certificate for Consumers and Producers via
# their Certificate Requests.
#  Consumers 
   openssl ca -batch -days 1820 -cert cacert.pem -keyfile caprivkey.pem \
   -in csrConsumer.pem -out Consumer-ca-cert.pem
#  Producers
   openssl ca -batch -days 1820 -cert cacert.pem -keyfile caprivkey.pem \
   -in csrProducer.pem -out Producer-ca-cert.pem

   openssl ca -batch -days 1820 -cert cacert.pem -keyfile caprivkey.pem \
   -in csrProducerLocalhost.pem -out ProducerLocalhost-ca-cert.pem

# Rewrite the certificates in PEM only format. This allows us to concatenate
# them into chains.
	openssl x509 -in cacert.pem -out cacert.pem -outform PEM
    openssl x509 -in Consumer-ca-cert.pem -out Consumer-ca-cert.pem -outform PEM
    openssl x509 -in Producer-ca-cert.pem -out Producer-ca-cert.pem -outform PEM
    openssl x509 -in ProducerLocalhost-ca-cert.pem -out ProducerLocalhost-ca-cert.pem -outform PEM

# Create a chain readable by CertificateFactory.getCertificates.
    cat Consumer-ca-cert.pem cacert.pem > Consumer.chain
    cat Producer-ca-cert.pem cacert.pem > Producer.chain
    cat ProducerLocalhost-ca-cert.pem cacert.pem > ProducerLocalhost.chain

# Replace the certificates in the client and service keystores with their respective
# full chains.
    keytool -import -file Consumer.chain -keystore consumer.jks -storetype jks \
    -alias Consumer \
    -storepass password -keypass password -noprompt

    keytool -import -file Producer.chain -keystore producer.jks -storetype jks \
    -alias Producer \
    -storepass password -keypass password -noprompt

    keytool -import -file ProducerLocalhost.chain -keystore producer-localhost.jks -storetype jks \
    -alias ProducerLocalhost \
    -storepass password -keypass password -noprompt

# Create the Truststore file containing the CA cert.
    keytool -import -file cacert.pem -alias TheCA -keystore truststore.jks \
    -storepass password -noprompt

# List keystores for manual inspection
    keytool -v -list -keystore consumer.jks -storepass password > consumer.jks.log
    keytool -v -list -keystore producer.jks -storepass password > producer.jks.log
    keytool -v -list -keystore producer-localhost.jks -storepass password > producerlocalhost.jks.log
    keytool -v -list -keystore truststore.jks -storepass password > truststore.jks.log

