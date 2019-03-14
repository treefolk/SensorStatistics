package com.sensorstatistics.core

import com.sensorstatistics.core.SensorReportsReader.Row

object SensorsStatistic {

  def statistic(rows: Iterator[Row]): Accumulator = {
    rows.foldLeft(Accumulator()) {

      case (acc, Row(id, Some(value))) =>
        val Stats(count, sum, min, max) = acc.sensors.getOrElse(id, Stats())
        val newStats = Stats(count + 1, sum + value, min.min(value), max.max(value))
        acc.copy(count = acc.count + 1,
          sensors = acc.sensors + {
          id -> newStats
        })

      case (acc, Row(id, None)) =>
        val newStats = acc.sensors.getOrElse(id, Stats(count = 1))
        acc.copy(count = acc.count + 1,
          failed = acc.failed + 1,
          sensors = acc.sensors + {
            id -> newStats
          })
    }
  }

  case class Accumulator(count: Int = 0, failed: Int = 0, sensors: Map[String, Stats] = Map.empty) {
    override def toString: String =
      s"""Num of processed measurements: $count
         |Num of failed measurements: $failed
         |
         |Sensors with highest avg humidity:
         |
         |sensor-id,min,avg,max
         |${sensors.toSeq.sortBy(_._2.avg).reverse.map { case (id, stats) => s"$id,$stats" }.mkString("\n")}
       """.stripMargin.trim
  }

  case class Stats(count: Long = 0, sum: Long = 0, min: Int = Int.MaxValue, max: Int = Int.MinValue) {
    lazy val avg: Int = (sum / count).toInt
    lazy val nan: Boolean = count > 0 && sum == 0 && min == Int.MaxValue && max == Int.MinValue

    override def toString: String = if (nan) s"NaN,NaN,NaN" else s"$min,$avg,$max"
  }

}
