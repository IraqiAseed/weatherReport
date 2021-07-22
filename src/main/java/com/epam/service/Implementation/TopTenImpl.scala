package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.TopTen
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import scala.collection.JavaConverters._

@Component
class TopTenImpl(eventRepository: EventRepository, farmRepository: FarmRepository,
              @Value("${topTen_PerUser_file_path}") TopTenUserOutPath:String ) extends TopTen {


  override def topTenPerFarm(farmId: Int): java.util.List[java.lang.Double] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

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
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService: StationsService = new StationsService
    val stations = stationService.getAllStations(farms, name, lastName)


    val topX = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .groupBy("stationId")
      .agg(sort_array(collect_list("value"), asc = false).alias("sortedTemperature"))

    topX.show()

    topX.printSchema()
    topX.describe()

    val topTen = topX
      .withColumn("maxTenTemperature", slice(col("sortedTemperature"), 1, 10))
      .drop("sortedTemperature")

    topTen.write.mode(SaveMode.Overwrite).json(TopTenUserOutPath) //SaveMode.Append
    topTen.toJSON
  }
}
