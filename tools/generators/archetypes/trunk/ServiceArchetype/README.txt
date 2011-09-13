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

user.dir: can be . or any other path where service should be generated
artifactId: Name of the new service, e.g CancelBooking
domain: The main domain, e.g crm
subdomain: the sub domain, e.g scheduling

3. Finally, generate the service by entering the "user.dir/artifactId" directory and do mvn install

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
