package com.epam.controller;

import com.epam.service.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basic/")
public class BasicControllerData {

    //todo : saving results to json files

    @Autowired
    private Getter getterService;

    @GetMapping("farms")
    public void getFarms() {
        getterService.getAllFarmsData();
    }

    @GetMapping("events")
    public void getEvents() {
        getterService.getAllEvents();
    }

    @GetMapping("crops")
    public void getCorps() {
        getterService.getCrops();
    }

    @GetMapping("users")
    public void getUsersData() {
        getterService.getUsersData();
    }

    @GetMapping("farmsStations")
    public void getFarmsStations() {
        getterService.getFarmsStations();
    }

    @GetMapping("eventsStations")
    public void getEventsStations() {
        getterService.getEventsStations();
    }

    @GetMapping("farmsLocations")
    public void getFarmsLocations() {
        getterService.getFarmsLocations();
    }
}
