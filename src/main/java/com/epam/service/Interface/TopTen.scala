package com.epam.service.Interface

import org.apache.spark.sql.Dataset
import org.springframework.beans.factory.annotation.Value

trait TopTen {

  def topTenPerFarm(farmId: Int): java.util.List[java.lang.Double]

  def topTenPerUser(name: String, lastName: String): Dataset[String]
}
