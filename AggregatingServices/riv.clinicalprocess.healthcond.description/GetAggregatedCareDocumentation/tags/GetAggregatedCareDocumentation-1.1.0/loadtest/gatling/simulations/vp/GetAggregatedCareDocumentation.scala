package vp

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

class GetAggregatedCareDocumentationSimulation extends Simulation {

  val testTimeSecs   = 7
  val noOfUsers      = 1
  val rampUpTimeSecs = 10
	val minWaitMs      = 5000 milliseconds
  val maxWaitMs      = 10000 milliseconds

  //Prod-url
    val httpConf = httpConfig
      .baseURL("https://192.168.25.10:20000")

  val skltp_headers = Map(
    "Accept-Encoding" -> "gzip,deflate",
    "Content-Type" -> "text/xml;charset=UTF-8",
    "SOAPAction" -> "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentationResponder:2:GetCareDocumentation",
    "x-vp-sender-id" -> "sid",
    "x-rivta-original-serviceconsumer-hsaid" -> "oid",
		"Keep-Alive" -> "115")

	val scn = scenario("Scenario name")
    .during(testTimeSecs) { 		
      exec(
        http("GetAggregatedCareDocumentation")
          .post("/vp/clinicalprocess/healthcond/description/GetCareDocumentation/2/rivtabp21")
  				.headers(skltp_headers)
          .fileBody("GetCareDocumentation_Positive_Response.xml").asXML
  				.check(status.is(200))
          .check(xpath("soap:Envelope", List("soap" -> "http://schemas.xmlsoap.org/soap/envelope/")).exists)
          .check(xpath("//hdr:ProcessingStatusList", List("hdr" -> "urn:riv:interoperability:headers:1")).count.is(2))
          .check(xpath("//resp:GetCareDocumentationResponse", List("resp" -> "urn:riv:clinicalprocess:healthcond:description:GetCareDocumentationResponder:2")).count.is(1))
      )
      .pause(minWaitMs, maxWaitMs)
    }
  	setUp(scn.users(noOfUsers).ramp(rampUpTimeSecs).protocolConfig(httpConf))
}