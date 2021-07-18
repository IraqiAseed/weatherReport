package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.Sequences
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, count, lag, monotonicallyIncreasingId, monotonically_increasing_id, row_number, when}
import org.apache.spark.storage.StorageLevel
import org.springframework.stereotype.Component

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@Component
class SequencesImpl(eventRepository: EventRepository, farmRepository: FarmRepository) extends Sequences {
  override def checkIfHasDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Boolean = {
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


    val windowSpec = Window.partitionBy("stationId").orderBy("datetime")

    var eventsSequences: Dataset[Row] = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .withColumn("prev-value", lag("value", 1).over(windowSpec))

      .withColumn("compare", when(col("value").geq(col("prev-value")), 1).otherwise(0))
      .withColumn("prev-compare", lag("compare", 1).over(windowSpec))
      .persist(StorageLevel.MEMORY_AND_DISK)

    eventsSequences.show()

    val breaks = eventsSequences.filter((col("compare").equalTo(0)).and(col("prev-compare")
      .equalTo(1))).count()

    println(breaks)

    breaks > 0

  }

  override def getFirstEventsDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Dataset[String] = {
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


    val windowSpec = Window.partitionBy("stationId").orderBy("datetime")

    var eventsSequences: Dataset[Row] = events
      .filter(col("stationId").isInCollection(stations))
      .filter(col("channel").equalTo("TG"))
      .withColumn("prev-value", lag("value", 1).over(windowSpec))

      .withColumn("compare", when(col("value").geq(col("prev-value")), 1).otherwise(0))
      .withColumn("prev-compare", lag("compare", 1).over(windowSpec))
      .withColumn("prev-datetime", lag("datetime", 1).over(windowSpec))

      .persist(StorageLevel.MEMORY_AND_DISK)

    eventsSequences.show()

    val breaks = eventsSequences.filter((col("compare").equalTo(0)).and(col("prev-compare")
      .equalTo(1))).count()

    println(breaks)

    if(breaks == 0)
      return null

    val breakFirstEvent = eventsSequences.filter((col("compare").equalTo(0))
      .and(col("prev-compare").equalTo(1)))
      .withColumn("index", row_number().over(windowSpec))
      .where(col("index").equalTo(1))
      .withColumnRenamed("datetime","end-datetime")
      .select("stationId","end-datetime")



    var startFirstEvent = eventsSequences
      .filter(col("compare").equalTo(1))
      .filter((col("prev-compare").equalTo(0)).or(col("prev-compare").equalTo(null)))
      .withColumn("index", row_number().over(windowSpec))
      .where(col("index").equalTo(1))
      .drop("datetime")
      .withColumnRenamed("prev-datetime","start-datetime")
      .select("stationId","start-datetime")



    val firstEventTimeInterval = startFirstEvent.join(breakFirstEvent,"stationId")
    firstEventTimeInterval.show()


    firstEventTimeInterval.toJSON
  }
}
