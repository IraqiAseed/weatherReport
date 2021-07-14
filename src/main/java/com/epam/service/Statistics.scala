package com.epam.service

import org.apache.spark.sql.{Dataset, Row}

trait Statistics {

  def countFarms: Int

  def relevantEvents: Dataset[Row] //farm stationId == event stationId

  def rainAverage(id: Int): Double //enter farm id return rain average from events

  def rainInFarmInSpecificPeriod(id: Int): Double

  def rainInFarmsInSpecificPeriod(): Dataset[Row]

  def maxMinTempFarm(id: Int): Double

  def maxMinTempAllFarms(id: Int): Double

}
