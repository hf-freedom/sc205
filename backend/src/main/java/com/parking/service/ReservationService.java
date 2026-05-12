package com.parking.service;

import com.parking.config.ParkingConfig;
import com.parking.entity.ParkingSpace;
import com.parking.entity.Reservation;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private ParkingConfig config;
    
    public Reservation createReservation(String plateNumber, String vehicleType, String parkingSpaceId,
                                          LocalDateTime expectedEntryTime, String contactPhone) {
        ParkingSpace space = storage.parkingSpaces.get(parkingSpaceId);
        if (space == null || !"AVAILABLE".equals(space.getStatus())) {
            return null;
        }
        
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setPlateNumber(plateNumber);
        reservation.setVehicleType(vehicleType);
        reservation.setParkingSpaceId(parkingSpaceId);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setExpectedEntryTime(expectedEntryTime);
        reservation.setExpiredTime(expectedEntryTime.plusMinutes(config.getReservationTimeoutMinutes()));
        reservation.setStatus("ACTIVE");
        reservation.setContactPhone(contactPhone);
        
        space.setStatus("RESERVED");
        
        storage.reservations.put(reservation.getId(), reservation);
        return reservation;
    }
    
    public Reservation cancelReservation(String reservationId) {
        Reservation reservation = storage.reservations.get(reservationId);
        if (reservation == null || !"ACTIVE".equals(reservation.getStatus())) {
            return null;
        }
        
        reservation.setStatus("CANCELLED");
        
        ParkingSpace space = storage.parkingSpaces.get(reservation.getParkingSpaceId());
        if (space != null && "RESERVED".equals(space.getStatus())) {
            space.setStatus("AVAILABLE");
        }
        
        return reservation;
    }
    
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(storage.reservations.values());
    }
    
    public List<Reservation> getActiveReservations() {
        return storage.reservations.values().stream()
                .filter(r -> "ACTIVE".equals(r.getStatus()))
                .collect(Collectors.toList());
    }
}
