

# Introduction #

Virtual services is a...

## Maven naming conventions ##

Convention:
```
<groupId>se.skl.skltpservices.<domain>.<subdomain></groupId>
<artifactId><service></artifactId>
<name><service> service</name>
```

Example:
```
<groupId>se.skl.skltpservices.ehr.blocking</groupId>
<artifactId>CheckBlocks</artifactId>
<name>CheckBlocks service</name>
```

## Endpoint adress naming conventions ##

RIV TA provides only rules regarding the version part of the endpoint adress. For SKLTP the following naming convention is used for endpoint adresses.

`https://host:port/vp/<domain>/<subdomain>/<extra subdomain>/<ServiceInteractionName>/<version>/<rivtaversion>`

`https://localhost:20000/vp/ehr/blocking/administration/CancelTemporaryExtendedRevoke/2/rivtabp21`


# Resources #

  * [How to deploy](HowToDeployServices.md)