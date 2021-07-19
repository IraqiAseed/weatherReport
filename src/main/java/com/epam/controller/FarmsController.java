package com.epam.controller;

import com.epam.model.FarmJava;
import com.epam.service.Implementation.FarmJavaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FarmsController {
    private FarmJavaService farmService;

    @Autowired
    public FarmsController(FarmJavaService farmService){
        this.farmService = farmService;
    }

    @GetMapping("/farms")
    public Iterable<FarmJava> getAllFarms() {
        return farmService.getAllFarms();
    }
}
