package com.epam.controller;


import com.epam.repository.EventRepository;
import com.epam.repository.FarmRepository;
import com.epam.service.Jumps;
import com.epam.service.JumpsImp;
import com.epam.service.StatisticsImpl;
import com.epam.service.TopTenImpl;
import org.apache.spark.sql.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/biUser/")
public class ReportsController {

    @Autowired
    private FarmRepository farmRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private TopTenImpl topTen;

    @Autowired
    private StatisticsImpl statistics;

    @Autowired
    private JumpsImp jumps;

    @GetMapping("topTenTemp")   //todo fix the object .. !! //todo saving to json file
    public List<Object> getTopTen(@RequestParam Integer farmId) {

        return topTen.topTenPerFarm(farmId);
    }

    @GetMapping("deviation") //todo - currentt is checking for temprature
    public String  getStandardDeviation(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = statistics.StandardDeviation(name,lastName);
        return DsStr.collectAsList().toString();

    }

    @GetMapping("jumps")
    public String  getConsecutiveEventsWithTemperatureJump(@RequestParam String name, @RequestParam String lastName,
                                                           @RequestParam Integer jump) {

        Dataset<String> DsStr = jumps.getConsecutiveEventsWithTemperatureJump(name,lastName,jump);
        return DsStr.collectAsList().toString();

    }


}
