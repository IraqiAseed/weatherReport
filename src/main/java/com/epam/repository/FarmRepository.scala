package com.epam.repository

import com.epam.model.Farm
import com.mongodb.spark.MongoSpark
import org.apache.spark
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.bson.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
@Component
class FarmRepository(sparkSession: SparkSession, @Value("${farms_file_path}") path:String) {

  def readEvents(): Dataset[Row] = {

    val schema = spark.sql.Encoders.product[Farm].schema

    sparkSession.read.option("multiline", "true").schema(schema).json(path)

  }
/*
  def writeToMongoDbFarmData() : Unit = {

    val docs = """
    |{"id": 12,"name": "Aseed","lastName": "Iraqi","stationId": 8,"location": "Sharon","crops": "mango","sensitivity": {"heat": 3,"cold": 4,"dry": 1},"email": "iraqiaseed@gmail.com"}
    |{"id": 15,"name": "Aseed","lastName": "Iraqi","stationId": 9,"location": "Sharon","crops": "mango","sensitivity": {"heat": 3,"cold": 4,"dry": 1},"email": "iraqiaseed@gmail.com"}
    |{"id": 24,"name": "Dekel","lastName": "Levitan","stationId": 3,"location": "South","crops": "tomato","sensitivity": {"cold": 4,"dry": 1},"email": "dekel@gmail.com"}
    |{"id": 77,"name": "Ifat","lastName": "Tankel","stationId": 7,"location": "North","crops": "corn","sensitivity": {"dry": 1,"heat": 2},"email": "ifat@gmail.com"}""".trim.stripMargin.split("[\\r\\n]+").toSeq

    MongoSpark.save(sparkSession.sparkContext.parallelize(docs.map(Document.parse)))


  }


 */
  def readFarmsDataFromMongoDb():Dataset[Row] = {
    val df = MongoSpark.load(sparkSession)
    df.printSchema()
    df.show(100)
    df
  }

}
