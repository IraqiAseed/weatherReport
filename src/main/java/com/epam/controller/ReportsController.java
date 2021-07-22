package com.epam.controller;


import com.epam.service.Implementation.JumpsImpl;
import com.epam.service.Implementation.SequencesImpl;
import com.epam.service.Implementation.StatisticsImpl;
import com.epam.service.Implementation.TopTenImpl;
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
    private TopTenImpl topTen;

    @Autowired
    private StatisticsImpl statistics;

    @Autowired
    private JumpsImpl jumps;

    @Autowired
    private SequencesImpl seq;

    @GetMapping("farmTopTenTemp")   //todo saving to json file
    public List<Double> getTopTen(@RequestParam Integer farmId) {

        return topTen.topTenPerFarm(farmId);
    }

    @GetMapping("userTopTenTemp")
    public List<String> topTen(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = topTen.topTenPerUser(name, lastName);
        return DsStr.collectAsList();

    }

    @GetMapping("deviation")
    public List<String> getStandardDeviation(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = statistics.standardDeviation(name, lastName);
        return DsStr.collectAsList();

    }

    @GetMapping("jumps")
    public List<String> getConsecutiveEventsWithTemperatureJump(@RequestParam String name, @RequestParam String lastName,
                                                                @RequestParam Integer jump) {

        Dataset<String> DsStr = jumps.getConsecutiveEventsWithTemperatureJump(name, lastName, jump);
        return DsStr.collectAsList();

    }

    @GetMapping("hasAscSeqThenDesc")
    public boolean checkIfHasDecreasingSequenceFollowedByAscendingSequences(@RequestParam String name, @RequestParam String lastName) {

        return seq.checkIfHasDecreasingSequenceFollowedByAscendingSequences(name, lastName);
        //return DsStr.collectAsList().toString();

    }

    @GetMapping("getAscSeqThenDesc")
    public List<String> getFirstEventDecreasingSequenceFollowedByAscendingSequences(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = seq.getFirstEventsDecreasingSequenceFollowedByAscendingSequences(name, lastName);
        return DsStr.collectAsList();

    }

    @GetMapping("avg")
    public List<String> getAverages(@RequestParam String name, @RequestParam String lastName) {

        Dataset<String> DsStr = statistics.average(name, lastName);
        return DsStr.collectAsList();

    }


}
