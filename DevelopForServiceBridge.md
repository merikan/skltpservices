

# Introduction #
This page contains information needed to develop services for the service bridge (Tjänstebryggan). Existing services can be found at [bridged interactions](http://code.google.com/p/skltpservices/source/browse/#svn%2FServiceInteractions%2Fbridgedinteractions)

# Develop services #

## Deployable artifact ##

A deployable artifact for the service bridge is a Java jar file containing a service definition file called **`tp-virtuell-tjanst-config.xml`**. The name of the file is important since service bridge vill scan for this name and initialize the services found.

## Sample service definition ##

Some values in the service definition is resolved from system configuration file but is good to be familiar with.

  * **tb.host**, the host that service bridge is configured to run under
  * **tb.port**, the port that service bridge is configured to run under
  * **tb.baseUri**, the uri to service bridge

Together these properties can form a url like:

```
https://localhost:10000/tjanstebryggan
```

Sample tp-virtuell-tjanst-config.xml:

```
<mule xmlns="http://www.mulesource.org/schema/mule/core/2.2"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:spring="http://www.springframework.org/schema/beans"
	xmlns:cxf="http://www.mulesource.org/schema/mule/cxf/2.2" xmlns:https="http://www.mulesource.org/schema/mule/https/2.2"
	xsi:schemaLocation="
               http://www.springframework.org/schema/beans    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
               http://www.mulesource.org/schema/mule/core/2.2 http://www.mulesource.org/schema/mule/core/2.2/mule.xsd
               http://www.mulesource.org/schema/mule/https/2.2 http://www.mulesource.org/schema/mule/https/2.2/mule-https.xsd
               http://www.mulesource.org/schema/mule/cxf/2.2  http://www.mulesource.org/schema/mule/cxf/2.2/mule-cxf.xsd">

	<model name="Domain-Subdomain-model">

		<service
			name="Domain-Subdomain-service">
			<inbound>
				<cxf:inbound-endpoint
					address="http://${tb.host}:${tb.port}/${tb.baseUri}/ServivceResponder/V1"
					proxy="true" payload="envelope" synchronous="true"
					applyTransformersToProtocol="false" />
			</inbound>
			<outbound>
				<pass-through-router>
					<cxf:outbound-endpoint
						address="adress to service enpoint"
						synchronous="true" proxy="true" payload="envelope"
						protocolConnector="TBProducerConnector"/>
				</pass-through-router>
			</outbound>
		</service>
	</model>
</mule>
```