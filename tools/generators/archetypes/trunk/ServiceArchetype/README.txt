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

mvn archetype:generate -DinteractiveMode=true -DarchetypeArtifactId=service-archetype -DarchetypeGroupId=se.skl.tp.archetype -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=se.skl.tp -DartifactId=CancelBooking -Dversion=1.0-SNAPSHOT -Ddomain=crm -Dsubdomain=scheduling


#MANDATORY PARAMETERS, needed to fill in#
Parameter: user.dir, can be . or any other path where service should be generated
Parameter: artifactId, Value: e.g CancelBooking
Parameter: groupId, Value: e.g se.skl.tp
Parameter: domain, Value: e.g crm
Parameter: subdomain, Value: e.g scheduling

#MANDATORY PARAMETERS, but with prefilled values that can be used in most cases#
Parameter: serviceMethodName, Value: e.g CancelBooking
Parameter: serviceInteractionName, Value: e.g CancelBookingInteraction
Parameter: serviceNamespace, Value: e.g urn:riv:crm:scheduling:CancelBooking:1:rivtabp20
Parameter: serviceWsdlFile, Value: e.g CancelBookingInteraction_1.0_RIVTABP20.wsdl
Parameter: serviceName, Value: e.g CancelBooking
Parameter: version, Value: e.g 1.0-SNAPSHOT
Parameter: serviceRelativePath, Value: e.g CancelBooking/1/rivtabp20
Parameter: package, Value: e.g se.skl.tp
Parameter: servicePackage, Value: e.g se.riv.crm.scheduling.v1

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
