package agp

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

class GetAggregatedDiagnosisSimulation_robustness extends Simulation {

  val testTimeSecs   = 43200 //12 hrs
  val noOfUsers      = 5 // gives 1 req/sec
  val rampUpTimeSecs = 120
	val minWaitMs      = 2000 milliseconds
  val maxWaitMs      = 5000 milliseconds

  // System under test
  val _baseURL        = "https://33.33.33.33:20000"
  val _contextPath    = "/vp/clinicalprocess/healthcond/description/GetDiagnosis/2/rivtabp21"

  //local producer
  //val _baseURL        = "http://localhost:20202"
  //val _contextPath    = "/producer_1/teststub/GetDiagnosis/2/rivtabp21"


  val httpConf = httpConfig
    .baseURL(_baseURL)
    .disableResponseChunksDiscarding
  
  val headers = Map(
    "Accept-Encoding" -> "gzip,deflate",
    "Content-Type" -> "text/xml;charset=UTF-8",
    "SOAPAction" -> "urn:riv:clinicalprocess:healthcond:description:GetDiagnosisResponder:2:GetDiagnosis",
    "x-vp-sender-id" -> "sid",
    "x-rivta-original-serviceconsumer-hsaid" -> "TP",
		"Keep-Alive" -> "115")


  val scn = scenario("GetAggregatedDiagnosis")
    .during(testTimeSecs) {
      feed(csv("patients.csv").random)
      .exec(
        http("GetAggregatedDiagnosis ${patientid} - ${name}")
          .post(_contextPath)
          .headers(headers)
          .fileBody("GetDiagnosis_request", Map("patientId" -> "${patientid}")).asXML
          .check(status.is(session => session.getTypedAttribute[String]("status").toInt))
          .check(xpath("soap:Envelope", List("soap" -> "http://schemas.xmlsoap.org/soap/envelope/")).exists)
          .check(xpath("//*[local-name()='diagnosis']", 
                         List("ns2" -> "urn:riv:clinicalprocess:healthcond:description:diagnosis:2")).count.is(session => session.getTypedAttribute[String]("count").toInt))
      )
      .pause(minWaitMs, maxWaitMs)
    }

  setUp(scn.users(noOfUsers).ramp(rampUpTimeSecs).protocolConfig(httpConf))

}