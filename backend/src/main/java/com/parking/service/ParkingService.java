package com.parking.service;

import com.parking.config.ParkingConfig;
import com.parking.entity.ParkingRecord;
import com.parking.entity.ParkingSpace;
import com.parking.entity.MonthlyCard;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private ParkingConfig config;
    
    public Optional<ParkingSpace> allocateParkingSpace(String plateNumber, String vehicleType) {
        String targetSpaceType = getTargetSpaceType(plateNumber, vehicleType);
        
        List<ParkingSpace> availableSpaces = storage.parkingSpaces.values().stream()
                .filter(s -> "AVAILABLE".equals(s.getStatus()))
                .sorted(Comparator.comparing(s -> {
                    if (targetSpaceType.equals(s.getSpaceType())) return 0;
                    if ("NORMAL".equals(s.getSpaceType())) return 1;
                    return 2;
                }))
                .collect(Collectors.toList());
        
        if (availableSpaces.isEmpty()) {
            return Optional.empty();
        }
        
        ParkingSpace space = availableSpaces.get(0);
        space.setStatus("OCCUPIED");
        space.setPlateNumber(plateNumber);
        space.setOccupiedTime(LocalDateTime.now());
        
        return Optional.of(space);
    }
    
    public String getTargetSpaceType(String plateNumber, String vehicleType) {
        if (isValidMonthlyCard(plateNumber)) {
            return "MONTHLY";
        }
        if ("TEMPORARY".equals(vehicleType)) {
            return "TEMPORARY";
        }
        return "NORMAL";
    }
    
    public boolean isValidMonthlyCard(String plateNumber) {
        return storage.monthlyCards.values().stream()
                .anyMatch(card -> card.getPlateNumber().equals(plateNumber)
                        && "ACTIVE".equals(card.getStatus())
                        && !card.getEndDate().isBefore(LocalDate.now()));
    }
    
    public Optional<MonthlyCard> getMonthlyCard(String plateNumber) {
        return storage.monthlyCards.values().stream()
                .filter(card -> card.getPlateNumber().equals(plateNumber))
                .findFirst();
    }
    
    public double calculateFee(String plateNumber, LocalDateTime entryTime, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes <= config.getFreeMinutes()) {
            return 0.0;
        }
        
        if (isValidMonthlyCard(plateNumber)) {
            return 0.0;
        }
        
        long chargeableMinutes = minutes - config.getFreeMinutes();
        long hours = (long) Math.ceil(chargeableMinutes / 60.0);
        double fee = hours * config.getHourlyRate();
        
        return Math.min(fee, config.getDailyCap());
    }
    
    public ParkingRecord createParkingRecord(String plateNumber, String vehicleType, ParkingSpace space) {
        ParkingRecord record = new ParkingRecord();
        record.setId(UUID.randomUUID().toString());
        record.setPlateNumber(plateNumber);
        record.setVehicleType(vehicleType);
        record.setParkingSpaceId(space.getId());
        record.setEntryTime(LocalDateTime.now());
        record.setStatus("PARKING");
        record.setBillingType(isValidMonthlyCard(plateNumber) ? "MONTHLY" : "TEMPORARY");
        
        storage.parkingRecords.put(record.getId(), record);
        return record;
    }
    
    public Optional<ParkingRecord> getActiveParkingRecord(String plateNumber) {
        return storage.parkingRecords.values().stream()
                .filter(r -> r.getPlateNumber().equals(plateNumber) && "PARKING".equals(r.getStatus()))
                .findFirst();
    }
    
    public List<ParkingRecord> getAllParkingRecords() {
        return new ArrayList<>(storage.parkingRecords.values());
    }
    
    public List<ParkingSpace> getAllParkingSpaces() {
        return new ArrayList<>(storage.parkingSpaces.values());
    }
    
    public void updateParkingSpace(ParkingSpace space) {
        storage.parkingSpaces.put(space.getId(), space);
    }
    
    public Optional<ParkingSpace> getParkingSpace(String spaceId) {
        return Optional.ofNullable(storage.parkingSpaces.get(spaceId));
    }
}
