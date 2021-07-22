package com.epam.service

import com.epam.repository.FarmRepository
import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.bson.Document
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class InitFarmService(sparkSession: SparkSession) {

  initMongoDB()


  def initMongoDB(): Unit = {
    val docs = """
                 |{"id": 12,"name": "Aseed","lastName": "Iraqi","stationId": 8,"location": "Sharon","crops": "mango","sensitivity": {"heat": 3,"cold": 4,"dry": 1},"email": "iraqiaseed@gmail.com"}
                 |{"id": 15,"name": "Aseed","lastName": "Iraqi","stationId": 9,"location": "Sharon","crops": "mango","sensitivity": {"heat": 3,"cold": 4,"dry": 1},"email": "iraqiaseed@gmail.com"}
                 |{"id": 24,"name": "Dekel","lastName": "Levitan","stationId": 3,"location": "South","crops": "tomato","sensitivity": {"cold": 4,"dry": 1},"email": "dekel@gmail.com"}
                 |{"id": 77,"name": "Ifat","lastName": "Tankel","stationId": 7,"location": "North","crops": "corn","sensitivity": {"dry": 1,"heat": 2},"email": "ifat@gmail.com"}""".trim.stripMargin.split("[\\r\\n]+").toSeq

    MongoSpark.save(sparkSession.sparkContext.parallelize(docs.map(Document.parse)))
    println("fsdfgdffhfhfhfhfh          dfgfdsd")
  }
}
