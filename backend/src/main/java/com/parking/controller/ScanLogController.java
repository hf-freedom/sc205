package com.parking.controller;

import com.parking.entity.Result;
import com.parking.entity.ScanLog;
import com.parking.service.ScanLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scan-log")
public class ScanLogController {
    
    @Autowired
    private ScanLogService scanLogService;
    
    @GetMapping("/list")
    public Result<List<ScanLog>> getAllScanLogs() {
        return Result.success(scanLogService.getAllScanLogs());
    }
    
    @GetMapping("/type/{scanType}")
    public Result<List<ScanLog>> getScanLogsByType(@PathVariable String scanType) {
        return Result.success(scanLogService.getScanLogsByType(scanType));
    }
    
    @GetMapping("/recent/{limit}")
    public Result<List<ScanLog>> getRecentScanLogs(@PathVariable int limit) {
        return Result.success(scanLogService.getRecentScanLogs(limit));
    }
}
