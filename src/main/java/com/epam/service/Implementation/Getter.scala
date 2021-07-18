package com.epam.service.Implementation

import com.epam.repository.{EventRepository, FarmRepository}
import com.epam.service.Interface.BasicData
import org.apache.spark.sql.{Dataset, Row}
import org.springframework.stereotype.Component

@Component
class Getter(eventRepository: EventRepository, farmRepository: FarmRepository) extends BasicData {

  override def getAllEvents: Dataset[Row] = {
    val events = eventRepository.readEvents()
    events.show()
    events
  }

  override def getAllFarmsData: Dataset[Row] = {

    val farms = farmRepository.readEvents()
    farms.show()
    print(farms.schema)
    farms

  }

  override def getCrops: Dataset[Row] = {
    val crops = farmRepository.readEvents().select("crops").distinct()
    crops.show()
    crops
  }

  override def getUsersData: Dataset[Row] = {
    val users = farmRepository.readEvents().select("name", "lastName", "email")
    users.show()
    users
  }

  override def getEventsStations: Dataset[Row] = {
    val stations = eventRepository.readEvents().select("stationId").distinct()
    stations.show()
    stations
  }

  override def getFarmsStations: Dataset[Row] = {
    val stations = farmRepository.readEvents().select("stationId").distinct()
    stations.show()
    stations
  }

  override def getFarmsLocations: Dataset[Row] = {
    val locations = farmRepository.readEvents().select("location").distinct()
    locations.show()
    locations
  }


}
