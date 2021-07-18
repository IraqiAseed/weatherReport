package com.epam.service.Interface

import org.apache.spark.sql.Dataset

trait Statistics {

  def standardDeviation(name: String, lastName: String): Dataset[String]

  def average(name: String, lastName: String): Dataset[String]


}
