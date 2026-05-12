package com.parking.service;

import com.parking.config.ParkingConfig;
import com.parking.entity.Statistics;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class StatisticsService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private ParkingConfig config;
    
    public Statistics getStatistics() {
        Statistics stats = new Statistics();
        
        stats.setTotalSpaces((long) storage.parkingSpaces.size());
        stats.setAvailableSpaces(storage.parkingSpaces.values().stream()
                .filter(s -> "AVAILABLE".equals(s.getStatus())).count());
        stats.setOccupiedSpaces(storage.parkingSpaces.values().stream()
                .filter(s -> "OCCUPIED".equals(s.getStatus())).count());
        stats.setLockedSpaces(storage.parkingSpaces.values().stream()
                .filter(s -> "LOCKED".equals(s.getStatus())).count());
        stats.setMaintenanceSpaces(storage.parkingSpaces.values().stream()
                .filter(s -> "MAINTENANCE".equals(s.getStatus())).count());
        stats.setReservedSpaces(storage.parkingSpaces.values().stream()
                .filter(s -> "RESERVED".equals(s.getStatus())).count());
        
        long nonMaintenance = stats.getTotalSpaces() - stats.getMaintenanceSpaces();
        if (nonMaintenance > 0) {
            double utilization = (double) stats.getOccupiedSpaces() / nonMaintenance * 100;
            stats.setSpaceUtilizationRate(Math.round(utilization * 100) / 100.0);
        } else {
            stats.setSpaceUtilizationRate(0.0);
        }
        
        double tempIncome = storage.parkingRecords.values().stream()
                .filter(r -> "COMPLETED".equals(r.getStatus()) && "TEMPORARY".equals(r.getBillingType()))
                .mapToDouble(r -> r.getActualFee() != null ? r.getActualFee() : 0)
                .sum();
        
        double monthlyIncome = storage.monthlyCards.values().stream()
                .filter(c -> "ACTIVE".equals(c.getStatus()))
                .mapToDouble(c -> c.getMonthlyFee() != null ? c.getMonthlyFee() : 0)
                .sum();
        
        stats.setTemporaryParkingIncome(Math.round(tempIncome * 100) / 100.0);
        stats.setMonthlyCardIncome(Math.round(monthlyIncome * 100) / 100.0);
        stats.setTotalIncome(stats.getTemporaryParkingIncome() + stats.getMonthlyCardIncome());
        
        long activeCards = storage.monthlyCards.values().stream()
                .filter(c -> "ACTIVE".equals(c.getStatus())).count();
        stats.setMonthlyCardCount(activeCards);
        
        long totalVehicles = storage.parkingRecords.values().stream()
                .map(r -> r.getPlateNumber())
                .distinct()
                .count();
        stats.setTotalVehicles(totalVehicles);
        
        if (totalVehicles > 0) {
            long monthlyVehicles = storage.parkingRecords.values().stream()
                    .filter(r -> "MONTHLY".equals(r.getBillingType()))
                    .map(r -> r.getPlateNumber())
                    .distinct()
                    .count();
            double ratio = (double) monthlyVehicles / totalVehicles * 100;
            stats.setMonthlyCardRatio(Math.round(ratio * 100) / 100.0);
        } else {
            stats.setMonthlyCardRatio(0.0);
        }
        
        stats.setAbnormalReleaseCount((long) storage.abnormalReleases.size());
        
        long currentParking = storage.parkingRecords.values().stream()
                .filter(r -> "PARKING".equals(r.getStatus()))
                .count();
        stats.setCurrentParkingVehicles(currentParking);
        
        return stats;
    }
}
