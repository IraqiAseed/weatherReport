package com.epam.service

import org.apache.spark.sql.{Dataset, Row}

trait SpecificData {

  def getEventsBetweenMinMaxTemp: Dataset[Row]

  def getEventsAtTimeDayBetweenMinMaxTemp: Dataset[Row]

  def getEventsHasRainGreaterThan: Dataset[Row]

  def getEventsTempGreaterThan: Dataset[Row]

  def getEventsWithNIPAndTempGreaterThan: Dataset[Row]

  def getFarmsBetweenMinMaxTemp: Dataset[Row]

  def getFarmsAtTimeDayBetweenMinMaxTemp: Dataset[Row]

  def getFarmsHasRainGreaterThan: Dataset[Row]

  def getFarmsTempGreaterThan: Dataset[Row]

  def getFarmsWithNIPAndTempGreaterThan: Dataset[Row]

}
