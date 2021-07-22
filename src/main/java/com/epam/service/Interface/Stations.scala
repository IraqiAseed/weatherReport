package com.epam.service.Interface

import org.apache.spark.sql.{Dataset, Row}

trait Stations {

  def getAllStations(farms: Dataset[Row],name:String,lastName:String): List[Int]

}
