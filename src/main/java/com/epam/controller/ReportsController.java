package com.epam.controller;


import com.epam.repository.EventRepository;
import com.epam.repository.FarmRepository;
import com.epam.service.Implementation.JumpsImp;
import com.epam.service.Implementation.SequencesImpl;
import com.epam.service.Implementation.StatisticsImpl;
import com.epam.service.Implementation.TopTenImpl;
import com.epam.service.Interface.Sequences;
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

    @Autowired
    private SequencesImpl seq;

    @GetMapping("farmTopTenTemp")   //todo fix the object .. !! //todo saving to json file
    public List<Object> getTopTen(@RequestParam Integer farmId) {

        return topTen.topTenPerFarm(farmId);
    }
    @GetMapping("userTopTenTemp")
    public String  topTen(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr =  topTen.topTenPerUser(name,lastName);
        return DsStr.collectAsList().toString();

    }

    @GetMapping("deviation") //todo - currentt is checking for temprature
    public String  getStandardDeviation(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = statistics.standardDeviation(name,lastName);
        return DsStr.collectAsList().toString();

    }

    @GetMapping("jumps")
    public String  getConsecutiveEventsWithTemperatureJump(@RequestParam String name, @RequestParam String lastName,
                                                           @RequestParam Integer jump) {

        Dataset<String> DsStr = jumps.getConsecutiveEventsWithTemperatureJump(name,lastName,jump);
        return DsStr.collectAsList().toString();

    }

    @GetMapping("hasAscSeqThenDesc")
    public boolean  checkIfHasDecreasingSequenceFollowedByAscendingSequences(@RequestParam String name, @RequestParam String lastName) {

        return  seq.checkIfHasDecreasingSequenceFollowedByAscendingSequences(name,lastName);
        //return DsStr.collectAsList().toString();

    }

    @GetMapping("getAscSeqThenDesc")
    public String  getFirstEventDecreasingSequenceFollowedByAscendingSequences(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr =  seq.getFirstEventsDecreasingSequenceFollowedByAscendingSequences(name,lastName);
        return DsStr.collectAsList().toString();

    }
    @GetMapping("avg")
    public String  getAverages(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr =  statistics.average(name,lastName);
        return DsStr.collectAsList().toString();

    }







}
