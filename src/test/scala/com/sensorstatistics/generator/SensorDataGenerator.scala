package com.sensorstatistics.generator

import java.io.{BufferedWriter, File, FileWriter}

object SensorDataGenerator extends App{

  val file = new File("src/test/resources/inputBigData/leader-3.csv")
  val bw = new BufferedWriter(new FileWriter(file))

  bw.write("sensor-id,humidity")

  0 to 100000000 foreach { i =>
    bw.write(s"\ns2,${i / 80}\ns3,NaN\ns2,78\ns1,98")
  }

  bw.close()


}
