package com.sensorstatistics.core

import java.io.File

import com.sensorstatistics.core.SensorReportsReader.Row
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

import scala.util.Success


class SensorReportsReaderSpec extends Specification {

  "ReportsReader" should {

    "get list of files with statistics from path" in new ReportsReaderScope {
      underTest.filesFrom("src/test/resources/inputData") must beEqualTo(
        Success(List(new File("src/test/resources/inputData/leader-2.csv"),
          new File("src/test/resources/inputData/leader-1.csv")))
      )
    }

    "throw exception when directory does not exist" in new ReportsReaderScope {
      underTest.filesFrom("src/test/resources/notExistingDir").get must
        throwA[IllegalArgumentException]("src/test/resources/notExistingDir does not exist.")

    }

    "get collection of rows from file" in new ReportsReaderScope {
      underTest.csv(new File("src/test/resources/inputData/leader-2.csv")).toList must beEqualTo(
        Vector(Row("s2", Some(80)), Row("s3", None), Row("s2", Some(78)), Row("s1", Some(98))))
    }

  }

  trait ReportsReaderScope extends Scope {
    val underTest: SensorReportsReader.type = SensorReportsReader
  }

}
