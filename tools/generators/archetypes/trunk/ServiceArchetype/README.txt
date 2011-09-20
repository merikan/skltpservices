Instructions for using the service-bundle-archetype
===================================================

The archetype creates a service for domain.subdomain.

|<service-name>
|   |
|   |-- <service-name>-virtualisering 

Create a service
================

1. Install the archetype.
- Enter the /skltpservices/tools/generators/archetypes/trunk/ServiceArchetype 
- run mvn install to install archetype on your system.

2. Navigate to a place where you want to create a new service and run this to create CancelBooking service

mvn archetype:generate 
-DinteractiveMode=true 
-DarchetypeArtifactId=service-archetype 
-DarchetypeGroupId=se.skl.tp.archetype 
-DarchetypeVersion=1.0-SNAPSHOT 
-DgroupId=se.skl.tp 
-DartifactId=crm-scheduling-CancelBooking 
-Dversion=1.0-SNAPSHOT 
-DdomainName=crm 
-DdomainSubName=scheduling
-DdomainServiceName=cancelbooking
-DserviceMethod=CancelBooking

#MANDATORY PARAMETERS, needed to fill in#
Parameter: user.dir, can be . or any other path where service should be generated
Parameter: groupId, Value: se.skl.tp should be used
Parameter: artifactId, Value: should be domain-subdomain-service e.g crm-scheduling-CancelBooking
Parameter: version, Value: 1.0-SNAPSHOT
Parameter: domainName, Value: the domain, e.g crm
Parameter: domainSubName, Value: the sub domain, e.g scheduling
Parameter: domainServiceName, Value: the service name in lower case, e.g cancelbooking (used in java package name for service)
Parameter: serviceMethod, Value: the service name in camel case, e.g CancelBooking (used in namespace and wsdl for service)

#MANDATORY PARAMETERS, but with prefilled values that can be used in most cases#
Parameter: serviceNamespace, Value: urn:riv:crm:scheduling:CancelBooking:1:rivtabp20
Parameter: serviceWsdlFile, Value: CancelBookingInteraction_1.0_rivtabp20.wsdl
Parameter: serviceInteraction, Value: CancelBookingInteraction
Parameter: serviceRelativePath, Value: CancelBooking/1/rivtabp20
Parameter: servicePackage, Value: se.riv.crm.scheduling.cancelbooking.v1

Import to Eclipse
=================

1. Make sure m2eclipse plugin is installed

2. Chose File->Import and select "Existing Maven project"

3. Point out your <service-name> directory, e.g /path/where/to/be/generated/CancelBooking

4. Import the projects.

Adjust Eclipse configuration
============================

1. Right click <service-name>-schemas project

2. Chose Maven->Update Project Configuration
