package com.sensorstatistics.core

import java.io.File

import scala.io.Source
import scala.util.Try

object SensorReportsReader {

  case class Row(id: String, value: Option[Int])

  def readFromFile(fileType: String, file: File): Iterator[Row] = {
    fileType match {
      case "csv" => csv(file)
      case _ => throw new IllegalStateException("Yet Not supported format")
    }
  }

  def csv(file: File, skipHeader: Boolean = true): Iterator[Row] = {
    val it = Source.fromFile(file).getLines
    val bodyIt = if (skipHeader) it.drop(1) else it
    bodyIt.map(_.split(",").toList).collect {
      case id :: value :: Nil =>
        val intValue = Try(value.toInt).toOption
        Row(id, intValue)
    }
  }

  def filesFrom(path: String): Try[Seq[File]] = Try {
    val root = new File(path)
    if (root.isDirectory) {
      root.listFiles(_.isFile).toList
    } else throw new IllegalArgumentException(s"$path does not exist.")
  }
}
