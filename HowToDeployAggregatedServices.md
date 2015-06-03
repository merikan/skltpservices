# Introduction #

This pages contains instructions on how to prepare, build and deploy aggregated services onto the Service Virtualization Platform for Service Oriented integration.

## Prereq deployment ##

These checkpoints needs to be verified before deployment in QA/Prod

  * Documentation
  * Tests (coverage etc)
  * License information is correct
  * Valid [SITHS](http://www.inera.se/Infrastrukturtjanster/SITHS/) Certificates are in place
  * [Mule ESB EE 3.3.1](http://www.mulesoft.com/) installed
  * [Active MQ](http://activemq.apache.org/) jar added to mule\_home/lib/user
  * A virtual service exists for the aggregated service

## Release mule app for aggregated services in domain ##

  * Using Maven release plugin

## Deploy QA ##

  * Build virtual services needed by aggregated services, based on [RIV TA](https://code.google.com/p/rivta/)
    * crm:scheduling 1.2-SNAPSHOT
  * [Order](http://www.inera.se/Infrastrukturtjanster/Tjansteplattform/Anslutning-till-Tjansteplattformen) connections to Service Virtualization Platform. Templates found [here](http://www.inera.se/Infrastrukturtjanster/Tjansteplattform/Dokument-Lankar)
  * Testdata/testmiljöer/soi-toolkit teststubbar?

  * Deploya Mule App
  * Override default properties needed by aggregated services (`GetAggregatedSubjectOfCareSchedule-config-override.properties`)
  * Update log properties (currently under classes folder in deployed app)

## Deploy Prod ##

## Development Environment ##
  * Mule Studio EE 1.3.1 with Mule ESB EE 3.3.1