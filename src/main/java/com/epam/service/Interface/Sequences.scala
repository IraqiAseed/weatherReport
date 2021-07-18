package com.epam.service.Interface

import org.apache.spark.sql.Dataset

trait Sequences {
  def checkIfHasDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Boolean
  def getFirstEventsDecreasingSequenceFollowedByAscendingSequences(name: String, lastName: String): Dataset[String]
}
