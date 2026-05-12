package com.parking.controller;

import com.parking.entity.ParkingSpace;
import com.parking.entity.Result;
import com.parking.service.ParkingService;
import com.parking.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/space")
public class ParkingSpaceController {
    
    @Autowired
    private ParkingService parkingService;
    
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    
    @GetMapping("/list")
    public Result<List<ParkingSpace>> getAllSpaces() {
        return Result.success(parkingService.getAllParkingSpaces());
    }
    
    @GetMapping("/{spaceId}")
    public Result<?> getSpace(@PathVariable String spaceId) {
        return parkingSpaceService.getSpace(spaceId)
                .map(Result::success)
                .orElseGet(() -> Result.error("车位不存在"));
    }
    
    @PostMapping("/lock")
    public Result<?> lockSpace(@RequestBody Map<String, String> params) {
        String spaceId = params.get("spaceId");
        ParkingSpace space = parkingSpaceService.lockSpace(spaceId);
        return space != null ? Result.success(space) : Result.error("无法锁定车位");
    }
    
    @PostMapping("/unlock")
    public Result<?> unlockSpace(@RequestBody Map<String, String> params) {
        String spaceId = params.get("spaceId");
        ParkingSpace space = parkingSpaceService.unlockSpace(spaceId);
        return space != null ? Result.success(space) : Result.error("无法解锁车位");
    }
    
    @PostMapping("/maintenance")
    public Result<?> setMaintenance(@RequestBody Map<String, String> params) {
        String spaceId = params.get("spaceId");
        ParkingSpace space = parkingSpaceService.setMaintenance(spaceId);
        return space != null ? Result.success(space) : Result.error("无法设置维修状态");
    }
    
    @PostMapping("/release-maintenance")
    public Result<?> releaseMaintenance(@RequestBody Map<String, String> params) {
        String spaceId = params.get("spaceId");
        ParkingSpace space = parkingSpaceService.releaseMaintenance(spaceId);
        return space != null ? Result.success(space) : Result.error("无法释放维修状态");
    }
}
