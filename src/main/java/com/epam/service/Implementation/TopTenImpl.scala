package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.TopTen
import org.apache.spark.sql.catalyst.expressions.Ascending
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel
import org.springframework.stereotype.Component

import scala.collection.JavaConverters._
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@Component
class TopTenImpl(eventRepository: EventRepository, farmRepository: FarmRepository) extends TopTen {


  override def topTenPerFarm(farmId: Int): java.util.List[java.lang.Double] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)

    val station: Int = farms
      .filter(col("id").equalTo(farmId))
      .select(col("stationId"))
      .distinct()
      .first()
      .getAs("stationId")


    val topX: List[Double] = events
      .filter(col("stationId").equalTo(station))
      .select("channel", "value")
      .filter(col("channel").equalTo("TG"))
      .sort(col("value").desc)
      .select("value")
      .collect
      .toList
      .map(r => r.getDouble(0))
      .take(10)


    print(topX)

    val top = topX.map(Double.box).asJava

    top
  }

  override def topTenPerUser(name: String, lastName: String): Dataset[String] = {

    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)

    val stations: List[Int] = farms
      .filter(col("name").equalTo(name))
      .filter(col("lastName").equalTo(lastName))
      .select(col("stationId"))
      .distinct()
      .collectAsList()
      .map(row => row.getInt(0))
      .toList

    println(stations)


    val topX = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .groupBy("stationId")
      .agg( sort_array(collect_list("value") ,asc=false).alias("sortedTemperature"))

    topX.show()

    topX.printSchema()
    topX.describe()

    val topTen = topX
      .withColumn("maxTenTemperature",slice(col("sortedTemperature"),1,10))
      .drop("sortedTemperature")


    topTen.toJSON
  }
}
