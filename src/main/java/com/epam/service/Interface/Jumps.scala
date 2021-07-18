package com.epam.service.Interface

import org.apache.spark.sql.Dataset

trait Jumps {

  def getConsecutiveEventsWithTemperatureJump(name: String, lastName: String, jump: Int): Dataset[String]
}
