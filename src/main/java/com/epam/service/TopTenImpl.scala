package com.epam.service

import com.epam.repository.{EventRepository, FarmRepository}
import org.apache.spark.sql.functions._
import org.springframework.stereotype.Component

import java.{lang, util}
import scala.collection.JavaConverters._
@Component
class TopTenImpl(eventRepository: EventRepository, farmRepository: FarmRepository) extends TopTen {
  override def topTenPerFarm(farmId:Int): java.util.List[Double] =
  {
    val events = eventRepository.readEvents()
    val farms = farmRepository.readEvents()

    val station:Int = farms
      .filter(col("id").equalTo(farmId))
      .select(col("stationId"))
      .distinct()
      .first()
      .getAs("stationId")


    val topX:List[Double] = events
      .filter(col("stationId").equalTo(station))
      .select("channel","value")
      .filter(col("channel").equalTo("TG"))
      .sort(col("value").desc)
      .select("value")
      .collect
      .toList
      .map(r => r.getDouble(0))
      .take(10)


    print(topX)

    val top = topX.asJava

    top
  }
}
