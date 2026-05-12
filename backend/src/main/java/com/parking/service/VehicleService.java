package com.parking.service;

import com.parking.config.ParkingConfig;
import com.parking.entity.*;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VehicleService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private ParkingService parkingService;
    
    @Autowired
    private ParkingConfig config;
    
    public Result<ParkingRecord> vehicleEntry(String plateNumber, String vehicleType, String contactPhone) {
        if (parkingService.getActiveParkingRecord(plateNumber).isPresent()) {
            return Result.error("该车辆已在场内");
        }
        
        Optional<ParkingSpace> reservedSpace = storage.reservations.values().stream()
                .filter(r -> r.getPlateNumber().equals(plateNumber) 
                        && "ACTIVE".equals(r.getStatus())
                        && r.getExpiredTime().isAfter(LocalDateTime.now()))
                .findFirst()
                .map(r -> storage.parkingSpaces.get(r.getParkingSpaceId()));
        
        if (reservedSpace.isPresent()) {
            ParkingSpace space = reservedSpace.get();
            space.setStatus("OCCUPIED");
            space.setPlateNumber(plateNumber);
            space.setOccupiedTime(LocalDateTime.now());
            
            storage.reservations.values().stream()
                    .filter(r -> r.getPlateNumber().equals(plateNumber) && "ACTIVE".equals(r.getStatus()))
                    .forEach(r -> {
                        r.setStatus("USED");
                        r.setActualEntryTime(LocalDateTime.now());
                    });
            
            ParkingRecord record = parkingService.createParkingRecord(plateNumber, vehicleType, space);
            return Result.success(record);
        }
        
        Optional<ParkingSpace> allocatedSpace = parkingService.allocateParkingSpace(plateNumber, vehicleType);
        if (!allocatedSpace.isPresent()) {
            return Result.error("没有可用车位");
        }
        
        ParkingRecord record = parkingService.createParkingRecord(plateNumber, vehicleType, allocatedSpace.get());
        return Result.success(record);
    }
    
    public Result<Map<String, Object>> vehicleExit(String plateNumber) {
        Optional<ParkingRecord> recordOpt = parkingService.getActiveParkingRecord(plateNumber);
        if (!recordOpt.isPresent()) {
            return Result.error("未找到该车辆的入场记录");
        }
        
        ParkingRecord record = recordOpt.get();
        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(record.getEntryTime(), exitTime).toMinutes();
        double totalFee = parkingService.calculateFee(plateNumber, record.getEntryTime(), exitTime);
        
        record.setExitTime(exitTime);
        record.setDurationMinutes(minutes);
        record.setTotalFee(totalFee);
        record.setActualFee(totalFee);
        record.setStatus("COMPLETED");
        
        ParkingSpace space = storage.parkingSpaces.get(record.getParkingSpaceId());
        if (space != null) {
            space.setStatus("AVAILABLE");
            space.setPlateNumber(null);
            space.setOccupiedTime(null);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("record", record);
        result.put("totalFee", totalFee);
        result.put("durationMinutes", minutes);
        result.put("isMonthlyCard", parkingService.isValidMonthlyCard(plateNumber));
        
        return Result.success(result);
    }
    
    public Result<ParkingRecord> abnormalRelease(String plateNumber, String reason, String operator, Double waivedFee) {
        Optional<ParkingRecord> recordOpt = parkingService.getActiveParkingRecord(plateNumber);
        if (!recordOpt.isPresent()) {
            return Result.error("未找到该车辆的入场记录");
        }
        
        ParkingRecord record = recordOpt.get();
        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(record.getEntryTime(), exitTime).toMinutes();
        double totalFee = parkingService.calculateFee(plateNumber, record.getEntryTime(), exitTime);
        
        record.setExitTime(exitTime);
        record.setDurationMinutes(minutes);
        record.setTotalFee(totalFee);
        record.setActualFee(waivedFee != null ? totalFee - waivedFee : 0.0);
        record.setStatus("ABNORMAL_RELEASE");
        record.setRemark(reason);
        
        ParkingSpace space = storage.parkingSpaces.get(record.getParkingSpaceId());
        if (space != null) {
            space.setStatus("AVAILABLE");
            space.setPlateNumber(null);
            space.setOccupiedTime(null);
        }
        
        AbnormalRelease abnormal = new AbnormalRelease();
        abnormal.setId(UUID.randomUUID().toString());
        abnormal.setParkingRecordId(record.getId());
        abnormal.setPlateNumber(plateNumber);
        abnormal.setReleaseTime(LocalDateTime.now());
        abnormal.setReason(reason);
        abnormal.setOperator(operator);
        abnormal.setWaivedFee(waivedFee != null ? waivedFee : totalFee);
        storage.abnormalReleases.put(abnormal.getId(), abnormal);
        
        return Result.success(record);
    }
    
    public List<ParkingRecord> getActiveVehicles() {
        List<ParkingRecord> records = new ArrayList<>();
        for (ParkingRecord record : storage.parkingRecords.values()) {
            if ("PARKING".equals(record.getStatus())) {
                records.add(record);
            }
        }
        return records;
    }
}
