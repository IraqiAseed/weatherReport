package com.epam.service.Implementation

import com.epam.service.Interface.Stations
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{Dataset, Row}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class StationsService extends Stations{
  override def getAllStations(farms: Dataset[Row],name:String,lastName:String): List[Int] = {
    val stations: List[Int] = farms
      .filter(col("name").equalTo(name))
      .filter(col("lastName").equalTo(lastName))
      .select(col("stationId"))
      .distinct()
      .collectAsList()
      .map(row => row.getInt(0))
      .toList

    println(stations)

    stations
  }
}
