Service bridge between TakeCare and RIV



# Introduction #

Mule flow...

# Installation instructions #

In this instruction `<MULE_HOME>` refers to the place where Mule is installed.

## Runtime environment ##

  * `Java(TM) SE Runtime Environment (build 1.6.0_33)`
  * `Mule ESB 3.2.1`
  * `ActiveMQ 5.4 (5.x)`

## Download distribution ##

Download the latest distribution from [TakeCareIntegrationComponent-1.0.2-RELEASE.zip](http://build.callistasoftware.org:8080/jenkins/job/TransformationServicesCrmSchedulingTakeCare-1.0.2/ws/TakeCareIntegrationComponent/target/TakeCareIntegrationComponent-1.0.2-RELEASE.zip)

## Build from source ##

Checkout the source code from xxx and build with yyy

## Deploy/Undeploy distribution ##

Drop the distribution ZIP file into `<MULE_HOME>`/apps folder. Mule unpack and deploy the app. Check the logfiles `<MULE_HOME>`/logs to verify no errors occurred during deploy.

To undeploy just remove the file named xxx-anchor.txt and Mule will remove the app.

## Configuration ##

To change the default settings for `TakeCare` crm:scheduling service bridge put a file with name _`TakeCareIntegrationComponent-config-override.properties`_ in the classpath (`<MULE_HOME>/conf`).

### SSL/TLS settings ###

| **Key** | **Default value if applicable** | **Description** |
|:--------|:--------------------------------|:----------------|
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_KEYSTORE |                                 | Repository of security certificates used in SSL communication  |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_KEYSTORE\_PASSWORD |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_KEY\_PASSWORD |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_KEY\_TYPE |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_TRUSTSTORE |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_TRUSTSTORE\_PASSWORD |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_TRUSTSTORE\_REQUIRE\_CLIENT\_AUTH |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_TLS\_TRUSTSTORE\_REQUIRE\_CLIENT\_AUTH |                                 |                 |
| SOITOOLKIT\_MULE\_HTTPS\_CLIENT\_SO\_TIMEOUT |                                 |                 |

### Service settings ###

| **Key** | **Default value if applicable** | **Description** |
|:--------|:--------------------------------|:----------------|
| SERVICE\_TIMEOUT\_MS | 8000 ms (8 sec)                 | Timeout for synchronous services |

**INBOUND\_URL** is the URL where the `TakeCare` crm:scheduling servicebridge accepts incoming requests.

**OUTBOUND\_URL** is the URL where `TakeCare` system accepts incoming requests.

| **Key** | **Default value if applicable** | **Description** |
|:--------|:--------------------------------|:----------------|
| GETALLTIMETYPES\_INBOUND\_URL | `http://localhost:8081/servicebridge/GetAllTimeTypes/1/rivtabp20`  |                 |
| GETALLTIMETYPES\_OUTBOUND\_URL |                                 |                 |
| GETSUBJECTOFCARESCHEDULE\_INBOUND\_URL | `http://localhost:8081/servicebridge/GetSubjectOfCareSchedule/1/rivtabp20` |                 |
| GETSUBJECTOFCARESCHEDULE\_OUTBOUND\_URL |                                 |                 |
| GETBOOKINGDETAILS\_INBOUND\_URL |  `http://localhost:8081/servicebridge/GetBookingDetails/1/rivtabp20` |                 |
| GETBOOKINGDETAILS\_OUTBOUND\_URL |                                 |                 |
| MAKEBOOKING\_INBOUND\_URL | `http://localhost:8081/servicebridge/MakeBooking/1/rivtabp20` |                 |
| MAKEBOOKING\_OUTBOUND\_URL |                                 |                 |
| CANCELBOOKING\_INBOUND\_URL | `http://localhost:8081/servicebridge/CancelBooking/1/rivtabp20` |                 |
| CANCELBOOKING\_OUTBOUND\_URL |                                 |                 |
| GETAVAILABLEDATES\_INBOUND\_URL | `http://localhost:8081/servicebridge/GetAvailableDates/1/rivtabp20` |                 |
| GETAVAILABLEDATES\_OUTBOUND\_URL |                                 |                 |
| GETAVAILABLETIMESLOTS\_INBOUND\_URL | `http://localhost:8081/servicebridge/GetAvailableTimeslots/1/rivtabp20` |                 |
| GETAVAILABLETIMESLOTS\_OUTBOUND\_URL |                                 |                 |
| UPDATEBOOKING\_INBOUND\_URL | `http://localhost:8081/servicebridge/UpdateBooking/1/rivtabp20` |                 |
| UPDATEBOOKING\_OUTBOUND\_URL |                                 |                 |