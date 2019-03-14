package com.sensorstatistics

import com.sensorstatistics.core.{SensorReportsReader, SensorsStatistic}

import scala.util.Try

object Application extends App {

  try {
    val statistic = process(args)
    println(statistic.get.message)
  } catch  {
    case illegalStateException: IllegalStateException => println(illegalStateException.getMessage)
    case ex : Exception => println(s"ERROR! Unexpected exception occurred with message $ex")
  }


  def process(args: Array[String]): Try[Statistic] = {
    args.toList match {
      case path :: Nil => SensorReportsReader.filesFrom(path).map { files =>
        val measurements = files.iterator.flatMap(file => SensorReportsReader.csv(file))
        val statistic = SensorsStatistic.statistic(measurements)
        Statistic(s"\nNum of processed files: ${files.length}\n$statistic")
      }
      case _ => throw new IllegalStateException("ERROR! Provide a path to humidity sensor data as one parameter.")
    }
  }
}

case class Statistic(message: String)

