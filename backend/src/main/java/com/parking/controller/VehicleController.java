package com.parking.controller;

import com.parking.entity.Result;
import com.parking.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @PostMapping("/entry")
    public Result<?> vehicleEntry(@RequestBody Map<String, Object> params) {
        String plateNumber = (String) params.get("plateNumber");
        String vehicleType = (String) params.get("vehicleType");
        if (vehicleType == null) {
            vehicleType = "NORMAL";
        }
        String contactPhone = (String) params.get("contactPhone");
        
        return vehicleService.vehicleEntry(plateNumber, vehicleType, contactPhone);
    }
    
    @PostMapping("/exit")
    public Result<?> vehicleExit(@RequestBody Map<String, String> params) {
        String plateNumber = params.get("plateNumber");
        return vehicleService.vehicleExit(plateNumber);
    }
    
    @PostMapping("/abnormal")
    public Result<?> abnormalRelease(@RequestBody Map<String, Object> params) {
        String plateNumber = (String) params.get("plateNumber");
        String reason = (String) params.get("reason");
        String operator = (String) params.get("operator");
        Double waivedFee = params.get("waivedFee") != null ? 
                Double.parseDouble(params.get("waivedFee").toString()) : null;
        
        return vehicleService.abnormalRelease(plateNumber, reason, operator, waivedFee);
    }
    
    @GetMapping("/active")
    public Result<?> getActiveVehicles() {
        return Result.success(vehicleService.getActiveVehicles());
    }
}
