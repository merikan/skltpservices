Instructions for using the service-bundle-archetype
===================================================

The bundle is, by default, providing an example for the
PingForConfiguration service. Each service bundle created
from the archetype will have the following structure:


|<service-name>
|   |
|   |-- <service-name>-virtualisering 

Create a service
================

1. Install the archetype.
- Enter the /skltpservices/tools/generators/archetypes/trunk/ServiceArchetype 
- run mvn install

The archetype is now installed on your system.

2. Navigate to a place where you want to create a new service and do

SAMPLE execution, replace user.dir, artifactId, domain and subdomain with correct values

mvn archetype:generate\
		-DinteractiveMode=true\
		-DarchetypeArtifactId=service-archetype\ 
		-DarchetypeGroupId=se.skl\ 
		-DarchetypeVersion=1.0-SNAPSHOT\ 
		-Duser.dir=/path/where/to/be/generated\ 
		-DgroupId=se.skl.tp\ 
		-DartifactId=CancelBooking\ 
		-Dversion=1.0-SNAPSHOT\
		-Ddomain=crm\ 
		-Dsubdomain=scheduling\

This should create the directory /path/where/to/be/generated/CancelBooking

3. Finally, generate the service by entering the /path/where/to/be/generated/CancelBooking directory and do mvn install

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
