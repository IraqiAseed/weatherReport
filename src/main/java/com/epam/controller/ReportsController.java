package com.epam.controller;


import com.epam.repository.EventRepository;
import com.epam.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class ReportsController {

    @Autowired
    private FarmRepository farmRepo;

    @Autowired
    private EventRepository eventRepo;

    @GetMapping("printFarms")
    public void printUsers() {
        farmRepo.readEvents().show();
    }

    @GetMapping("printEvents")
    public void printEvents() {
        eventRepo.readEvents().show();
    }
}
