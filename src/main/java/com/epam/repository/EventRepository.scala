package com.epam.repository

import com.epam.model.Event
import org.apache.spark
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
class EventRepository(sparkSession: SparkSession, @Value("${events_file_path}") path:String) {

  def readEvents(): Dataset[Row] = {

    val schema = spark.sql.Encoders.product[Event].schema
    sparkSession.read.schema(schema).json(path)
      //removed .option("multiline", "true") - they return a single line json !!

  }


}
