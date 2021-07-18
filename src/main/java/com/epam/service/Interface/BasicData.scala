package com.epam.service.Interface

import org.apache.spark.sql.{Dataset, Row}

trait BasicData {

  def getAllEvents: Dataset[Row]

  def getAllFarmsData: Dataset[Row]

  def getUsersData: Dataset[Row]

  def getCrops: Dataset[Row]

  def getEventsStations: Dataset[Row]

  def getFarmsStations: Dataset[Row]

  def getFarmsLocations: Dataset[Row]

}
