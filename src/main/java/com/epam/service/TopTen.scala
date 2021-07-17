package com.epam.service

trait TopTen {
  def topTenPerFarm(farmId:Int):java.util.List[Double]
}
