package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.Jumps
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Row, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JumpsImp(eventRepository: EventRepository, farmRepository: FarmRepository,
               @Value("${jumps_file_path}") jumpsOutPath:String,spark: SparkSession) extends Jumps {

  override def getConsecutiveEventsWithTemperatureJump(name: String, lastName: String, jump: Int): Dataset[String] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService:StationsService = new StationsService
    val stations = stationService.getAllStations(farms,name,lastName)

    val windowSpec = Window.partitionBy("stationId").orderBy("datetime")

    var eventsWithJump: Dataset[Row] = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .withColumn("prev-dateTime", when((lag("datetime", 1).over(windowSpec)).isNull, null)
        .otherwise(lag("datetime", 1).over(windowSpec)))
      .withColumn("diff", col("value") - when((lag("value", 1).over(windowSpec)).isNull, null)
        .otherwise(lag("value", 1).over(windowSpec)))
      .filter(col("diff").gt(jump))
      .withColumnRenamed("datetime","datetime2")
      //.withColumnRenamed("value","value2")
      .drop("channel")
      .drop("diff")
     .drop("value")
      .persist(StorageLevel.MEMORY_AND_DISK)

    eventsWithJump.show()


    var eventsAllDataThatJumps = events.join(broadcast(eventsWithJump),"stationId")
      .persist(StorageLevel.MEMORY_AND_DISK)


  //    var eventsAllDataThatJumps = events.join(eventsWithJump,"stationId")
  //    .persist(StorageLevel.MEMORY_AND_DISK)

    eventsAllDataThatJumps = eventsAllDataThatJumps
      .filter(col("datetime").equalTo( col("datetime2"))
        .or(col("datetime").equalTo( col("prev-dateTime"))))
      .drop("datetime2","prev-dateTime")
      .distinct()
      .orderBy(asc("datetime"))

    eventsAllDataThatJumps.show
    eventsAllDataThatJumps.write.mode(SaveMode.Overwrite).json(jumpsOutPath)
    eventsAllDataThatJumps.toJSON

  }
}
