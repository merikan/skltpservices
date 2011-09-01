Instructions for using the service-bundle archetype
===================================================

The bundle is, by default, providing an example for the
PingForConfiguration service. Each service bundle created
from the archetype will have the following structure:

<service-name>Bundle
|
|-- <service-name>-schemas
|
|-- <service-name>-service
|   |
|   |-- <service-name>-test-consumer
|   |
|   |-- <service-name>-test-producer
|   |
|   |-- <service-name>-virtualisering 

Each service bundle generate from the service-bundle archetype
will have this structure. However, before building the actual
service there are properties that must be set in order for the
project to build properly.

These properties are found in the root project, i.e <service-name>Bundle/pom.xml
All projects created from the service-bundle archetype will get the
PingForConfiguration example.

Create a service
================

1. Install the archetype. Enter the sklservice directory and do mvn install

The archetype is now installed on your system.

2. Navigate to a place where you want to create a new service and do
mvn archetype:create -DarchetypeGroupId=se.skl -DarchetypeArtifactId=service-bundle -DarchetypeVersion=1.0-SNAPSHOT\
 -DgroupId=se.skl.tp -DartifactId=<service-name> -Dversion=1.0-SNAPSHOT

This should create the directory ./<service-name

3. Edit the properties in pom.xml located in the <service-name> directory.

4. Finally, generate the service by entering the <service-name> directory and do mvn install

Import to Eclipse
=================

1. Make sure m2eclipse plugin is installed

2. Chose File->Import and select "Existing Maven project"

3. Point out your <service-name> directory

4. Import the projects.

Adjust Eclipse configuration
============================

1. Right click <service-name>-schemas project

2. Chose Maven->Update Project Configuration

Implement service
=================

1. Implement correct methods in ProducerImpl.java
