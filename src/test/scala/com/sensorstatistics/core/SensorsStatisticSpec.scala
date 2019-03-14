package com.sensorstatistics.core

import com.sensorstatistics.core.SensorReportsReader.Row
import com.sensorstatistics.core.SensorsStatistic.{Accumulator, Stats}
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class SensorsStatisticSpec extends Specification {

  "SensorsStatistic" should {

    "return statistic reports with 0 failed measurements" in new SensorsStatisticScope {
      val accumulator: Accumulator = underTest.statistic(Iterator(Row("s1", Some(80))))
      val stats: List[Stats] = accumulator.sensors.values.toList
      accumulator must beEqualTo(Accumulator(1, 0, Map("s1" -> Stats(1, 80, 80, 80))))
      stats.map(_.toString) must containTheSameElementsAs(List("80,80,80"))
    }

    "return statistic reports with 1 failed measurement" in new SensorsStatisticScope {
      val accumulator: Accumulator = underTest.statistic(Iterator(Row("s2", None)))
      val stats: List[Stats] = accumulator.sensors.values.toList
      accumulator must beEqualTo(Accumulator(1, 1, Map("s2" -> Stats(1, 0, Int.MaxValue, Int.MinValue))))
      stats.map(_.toString) must containTheSameElementsAs(List("NaN,NaN,NaN"))
    }

    "return statistic reports with 0 failed measurements when sensor humidity = 0" in new SensorsStatisticScope {
      val accumulator: Accumulator = underTest.statistic(Iterator(Row("s4", Some(0))))
      val stats: List[Stats] = accumulator.sensors.values.toList
      accumulator must beEqualTo(Accumulator(1, 0, Map("s4" -> Stats(1, 0, 0, 0))))
      stats.map(_.toString) must containTheSameElementsAs(List("0,0,0"))
    }

  }

  trait SensorsStatisticScope extends Scope {
    val underTest: SensorsStatistic.type = SensorsStatistic
  }
}
