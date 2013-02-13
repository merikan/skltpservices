A Proxy written in Java using Apache Camel to filter out mule session variables from HTTP Headers between
a mule-2.2.8 instance running SKLTP talking to a HTTP service running in a mule-3.x instance.
It will listen on port 8082 remove all X-MULE-SESSION variables and forward the request to port 8081.

To compile:
	$ mvn clean package

This will produce a .jar file in target called camel-examples-1.0-SNAPSHOT-jar-with-dependencies.jar

To run:
	$ java -jar camel-examples-1.0-SNAPSHOT-jar-with-dependencies.jar
