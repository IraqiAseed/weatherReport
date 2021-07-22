package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.Sequences
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.{Dataset, Row, SaveMode}
import org.apache.spark.sql.functions.{col, count, lag, monotonicallyIncreasingId, monotonically_increasing_id, row_number, when}
import org.apache.spark.storage.StorageLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@Component
class SequencesImpl(eventRepository: EventRepository, farmRepository: FarmRepository,
                    @Value("${Asc_then_dec_file_path}") seqAscThenDecOutPath:String) extends Sequences {


  def HasDecSeqFollowedByAscSeq(name: String, lastName: String,stations:List[Int] ,ws:WindowSpec,events:Dataset[Row],farms:Dataset[Row]):Dataset[Row] = {

    events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .withColumn("prev-value", lag("value", 1).over(ws))
      .withColumn("compare", when(col("value").geq(col("prev-value")), 1).otherwise(0))
      .withColumn("prev-compare", lag("compare", 1).over(ws))
      .withColumn("prev-datetime", lag("datetime", 1).over(ws))
  }


  override def checkIfHasDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Boolean = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService: StationsService = new StationsService
    val stations = stationService.getAllStations(farms, name, lastName)

    val windowSpec = Window.partitionBy("stationId").orderBy("datetime")

    val eventsSequences: Dataset[Row] = HasDecSeqFollowedByAscSeq(name,lastName,stations,windowSpec,events,farms)
      .persist(StorageLevel.MEMORY_AND_DISK)

    eventsSequences.show()

    val breaks = eventsSequences.filter((col("compare").equalTo(0)).and(col("prev-compare")
      .equalTo(1))).count()

    println(breaks)

    breaks > 0

  }

  override def getFirstEventsDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Dataset[String] = {
    val events = eventRepository.readEvents().persist(StorageLevel.MEMORY_AND_DISK)
    val farms = farmRepository.readFarmsDataFromMongoDb().persist(StorageLevel.MEMORY_AND_DISK)

    val stationService: StationsService = new StationsService
    val stations = stationService.getAllStations(farms, name, lastName)


    val windowSpec = Window.partitionBy("stationId").orderBy("datetime")

    val eventsSequences: Dataset[Row] = HasDecSeqFollowedByAscSeq(name,lastName,stations,windowSpec,events,farms)
      .persist(StorageLevel.MEMORY_AND_DISK)

    eventsSequences.show()

    val breaks = eventsSequences.filter((col("compare").equalTo(0)).and(col("prev-compare")
      .equalTo(1))).count()

    if (breaks == 0)
      return null

    val breakFirstEvent = eventsSequences.filter((col("compare").equalTo(0))
      .and(col("prev-compare").equalTo(1)))
      .withColumn("index", row_number().over(windowSpec))
      .where(col("index").equalTo(1))
      .withColumnRenamed("datetime", "end-datetime")
      .select("stationId", "end-datetime")


    val startFirstEvent = eventsSequences
      .filter(col("compare").equalTo(1))
      .filter((col("prev-compare").equalTo(0)).or(col("prev-compare").equalTo(null)))
      .withColumn("index", row_number().over(windowSpec))
      .where(col("index").equalTo(1))
      .drop("datetime")
      .withColumnRenamed("prev-datetime", "start-datetime")
      .select("stationId", "start-datetime")


    val firstEventTimeInterval = startFirstEvent.join(breakFirstEvent, "stationId")
    firstEventTimeInterval.show()
    firstEventTimeInterval.write.mode(SaveMode.Overwrite).json(seqAscThenDecOutPath)
    firstEventTimeInterval.toJSON
  }
}
