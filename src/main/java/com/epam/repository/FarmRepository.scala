package com.epam.repository

import com.epam.model.Farm
import org.apache.spark
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class FarmRepository(sparkSession: SparkSession, @Value("${farms_file_path}") path:String) {

  def readEvents(): Dataset[Row] = {

    val schema = spark.sql.Encoders.product[Farm].schema

    sparkSession.read.option("multiline", "true").schema(schema).json(path)


  }
}
