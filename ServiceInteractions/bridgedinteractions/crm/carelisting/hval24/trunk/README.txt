Below is a list of all endpoints for the hval adapter, and sample configuration. Make sure these addresses are loaded
and accessible in runtime. For Tjanstebryggan and Mule 2.2.8 these needs to be set in tb-config.properties.

#############
## Hval24  ##
#############

########################
# vard2hval GetListing #
########################

inbound.endpoint.hval24.getlisting.teststub=https://localhost:12000/tb/hval24/testproducer/GetListing/1/rivtabp20

inbound.endpoint.hval24.getlisting=http://localhost:11000/tb/carelisting/hval24/GetListing/1/rivtabp20
outbound.endpoint.hval24.getlisting=${inbound.endpoint.hval24.getlisting.teststub}