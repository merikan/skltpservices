package vp

import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import com.excilys.ebi.gatling.jdbc.Predef._
import com.excilys.ebi.gatling.http.Headers.Names._
import akka.util.duration._
import bootstrap._

class GetAggregatedMaternityMedicalHistorySimulation extends Simulation {

  val testTimeSecs   = 10
  val noOfUsers      = 5
  val rampUpTimeSecs = 10
	val minWaitMs      = 2000 milliseconds
  val maxWaitMs      = 5000 milliseconds

  // skltp-box
  //val _baseURL =       "https://33.33.33.33:20000"
  //val _contextPath     = "/vp/GetSubjectOfCareSchedule/1/rivtabp21"

  // NTjP Test
  val _baseURL        = "https://192.168.19.10:20000"
  val _contextPath     = "/vp/clinicalprocess/healthcond/actoutcome/GetMaternityMedicalHistory/2/rivtabp21"


  //local producer
  //val _baseURL        = "http://localhost:20202"
  //val _contextPath    = "/producer_1/teststub/GetMaternityMedicalHistory/2/rivtabp21"


  val httpConf = httpConfig
    .baseURL(_baseURL)
    .disableResponseChunksDiscarding
  
  val headers = Map(
    "Accept-Encoding" -> "gzip,deflate",
    "Content-Type" -> "text/xml;charset=UTF-8",
    "SOAPAction" -> "urn:riv:clinicalprocess:healthcond:actoutcome:GetMaternityMedicalHistoryResponder:2:GetMaternityMedicalHistory",
    "x-vp-sender-id" -> "sid",
    //"x-rivta-original-serviceconsumer-hsaid" -> "oid",
    "x-rivta-original-serviceconsumer-hsaid" -> "TP",
		"Keep-Alive" -> "115")


  val scn = scenario("GetAggregatedMaternityMedicalHistory")
    .during(testTimeSecs) {
      feed(csv("patients.csv").random)
      .exec(
        http("GetAggregatedMaternityMedicalHistory ${patientid} - ${name}")
          .post(_contextPath)
          .headers(headers)
          .fileBody("GetMaternityMedicalHistory_request", Map("patientId" -> "${patientid}")).asXML
          .check(status.is(session => session.getTypedAttribute[String]("status").toInt))
          .check(xpath("soap:Envelope", List("soap" -> "http://schemas.xmlsoap.org/soap/envelope/")).exists)
          .check(xpath("//*[local-name()='maternityMedicalRecordHeader']", 
                         List("ns2" -> "urn:riv:clinicalprocess:healthcond:actoutcome:2")).count.is(session => session.getTypedAttribute[String]("count").toInt))
      )
      .pause(minWaitMs, maxWaitMs)
    }

  setUp(scn.users(noOfUsers).ramp(rampUpTimeSecs).protocolConfig(httpConf))

}