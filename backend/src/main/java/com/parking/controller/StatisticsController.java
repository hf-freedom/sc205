package com.parking.controller;

import com.parking.entity.Result;
import com.parking.entity.Statistics;
import com.parking.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping
    public Result<Statistics> getStatistics() {
        return Result.success(statisticsService.getStatistics());
    }
}
