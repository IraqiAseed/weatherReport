package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.Statistics
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SaveMode}
import org.apache.spark.storage.StorageLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`


@Component
class StatisticsImpl(eventRepository: EventRepository, farmRepository: FarmRepository,
                     @Value("${standard_deviation_file_path}") standardDeviationOutPath:String,
                     @Value("${average_file_path}") averageOutPath:String) extends Statistics {

  override def standardDeviation(name: String, lastName: String): Dataset[String] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService:StationsService = new StationsService
    val stations = stationService.getAllStations(farms,name,lastName)

    val eventsForDeviation: Dataset[Row] = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .drop("channel", "datetime")
      .persist(StorageLevel.MEMORY_AND_DISK)


    val averageDF = eventsForDeviation
      .groupBy("stationId")
      .agg(avg("value").alias("average"))
      .persist(StorageLevel.MEMORY_AND_DISK)

    val eventsWithAvg = eventsForDeviation.join(averageDF, "stationId")
      .persist(StorageLevel.MEMORY_AND_DISK)


    eventsWithAvg.show()

    val eventDeviation = eventsWithAvg.withColumn("SquaredDistance",
      (col("value").minus(col("average"))).multiply(col("value").minus(col("average"))))
      .groupBy("stationId")
      .agg(
        sum("squaredDistance").alias("sum"),
        count("stationId").alias("count")
      )
      .withColumn("deviation", sqrt(col("sum").divide(col("count"))))
      .drop("sum", "count")
      .persist(StorageLevel.MEMORY_AND_DISK)


    eventDeviation.show()

    eventDeviation.write.mode(SaveMode.Overwrite).json(standardDeviationOutPath)

    eventDeviation.toJSON


  }

  override def average(name: String, lastName: String): Dataset[String] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService:StationsService = new StationsService
    val stations = stationService.getAllStations(farms,name,lastName)


    val eventsAvg: Dataset[Row] = events
      .filter(col("stationId").isInCollection(stations))
      .groupBy("stationId","channel")
      .agg(avg("value"))
      .sort("stationId")

    eventsAvg.show()
    eventsAvg.write.mode(SaveMode.Overwrite).json(averageOutPath)
    eventsAvg.toJSON

  }
}
