package com.epam.service

import org.apache.spark.sql.{Dataset, Row}

trait Jumps {

  def getConsecutiveEventsWithTemperatureJump(name: String,lastName:String, jump:Int):Dataset[String]
}
