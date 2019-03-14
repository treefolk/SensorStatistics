package com.sensorstatistics

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class ApplicationSpec extends Specification {

  "Application" should {

    "successfully return statistic report" in new ApplicationScope {
      val expectedMessage: String = "\nNum of processed files: 2\n" +
        "Num of processed measurements: 7\n" +
        "Num of failed measurements: 2\n\n" +
        "Sensors with highest avg humidity:\n\n" +
        "sensor-id,min,avg,max\n" +
        "s2,78,82,88\n" +
        "s1,10,54,98\n" +
        "s3,NaN,NaN,NaN"

      val pathToFiles = Array("src/test/resources/inputData")
      underTest.process(pathToFiles).get.message must beEqualTo(expectedMessage)
    }

    "throw exception when wrong args" in new ApplicationScope {
      val expectedMessage = "ERROR! Provide a path to humidity sensor data as one parameter."

      val inputParameters = Array("path", "path")
      underTest.process(inputParameters) should throwA[IllegalStateException](expectedMessage)
    }
  }

  trait ApplicationScope extends Scope {

    val underTest: Application.type = Application
  }

}
