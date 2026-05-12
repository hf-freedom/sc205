package com.parking.controller;

import com.parking.entity.ParkingRecord;
import com.parking.entity.Result;
import com.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {
    
    @Autowired
    private ParkingService parkingService;
    
    @GetMapping("/list")
    public Result<List<ParkingRecord>> getAllRecords() {
        return Result.success(parkingService.getAllParkingRecords());
    }
}
