package com.parking.service;

import com.parking.entity.ParkingSpace;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingSpaceService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private MonthlyCardService monthlyCardService;
    
    public ParkingSpace lockSpace(String spaceId) {
        ParkingSpace space = storage.parkingSpaces.get(spaceId);
        if (space == null || !"AVAILABLE".equals(space.getStatus())) {
            return null;
        }
        space.setStatus("LOCKED");
        return space;
    }
    
    public ParkingSpace unlockSpace(String spaceId) {
        ParkingSpace space = storage.parkingSpaces.get(spaceId);
        if (space == null || !"LOCKED".equals(space.getStatus())) {
            return null;
        }
        space.setStatus("AVAILABLE");
        return space;
    }
    
    public ParkingSpace setMaintenance(String spaceId) {
        ParkingSpace space = storage.parkingSpaces.get(spaceId);
        if (space == null) {
            return null;
        }
        if ("OCCUPIED".equals(space.getStatus())) {
            return null;
        }
        space.setStatus("MAINTENANCE");
        return space;
    }
    
    public ParkingSpace releaseMaintenance(String spaceId) {
        ParkingSpace space = storage.parkingSpaces.get(spaceId);
        if (space == null || !"MAINTENANCE".equals(space.getStatus())) {
            return null;
        }
        space.setStatus("AVAILABLE");
        return space;
    }
    
    public Optional<ParkingSpace> getSpace(String spaceId) {
        return Optional.ofNullable(storage.parkingSpaces.get(spaceId));
    }
}
