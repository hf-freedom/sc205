package com.parking.schedule;

import com.parking.config.ParkingConfig;
import com.parking.entity.ParkingSpace;
import com.parking.entity.Reservation;
import com.parking.entity.ScanLog;
import com.parking.service.MonthlyCardService;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ScheduledTaskService {
    
    @Autowired
    private ParkingStorage storage;
    
    @Autowired
    private MonthlyCardService monthlyCardService;
    
    @Autowired
    private ParkingConfig config;
    
    @Scheduled(fixedRate = 60000)
    public void scanExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<String> changedDetails = new ArrayList<>();
        int changedCount = 0;
        int totalScanned = storage.reservations.size();
        
        for (Reservation r : storage.reservations.values()) {
            if ("ACTIVE".equals(r.getStatus()) && r.getExpiredTime().isBefore(now)) {
                r.setStatus("EXPIRED");
                ParkingSpace space = storage.parkingSpaces.get(r.getParkingSpaceId());
                if (space != null && "RESERVED".equals(space.getStatus())) {
                    space.setStatus("AVAILABLE");
                    changedDetails.add("车位[" + space.getSpaceNo() + "] 预约已过期，已释放车位");
                    changedCount++;
                }
            }
        }
        
        if (changedCount > 0 || totalScanned > 0) {
            createScanLog("预约超时扫描", totalScanned, changedCount, changedDetails);
        }
    }
    
    @Scheduled(fixedRate = 300000)
    public void scanLongStayVehicles() {
        LocalDateTime now = LocalDateTime.now();
        long thresholdMinutes = config.getLongStayThresholdHours() * 60;
        List<String> changedDetails = new ArrayList<>();
        int changedCount = 0;
        int totalScanned = 0;
        
        for (ParkingSpace s : storage.parkingSpaces.values()) {
            if ("OCCUPIED".equals(s.getStatus()) && s.getOccupiedTime() != null) {
                totalScanned++;
                long minutes = Duration.between(s.getOccupiedTime(), now).toMinutes();
                s.setOccupiedDuration(minutes);
                
                if (minutes >= thresholdMinutes) {
                    changedDetails.add("车位[" + s.getSpaceNo() + "] 车辆长期占用，时长：" + (minutes / 60) + "小时");
                    changedCount++;
                }
            }
        }
        
        if (changedCount > 0 || totalScanned > 0) {
            createScanLog("长期占用扫描", totalScanned, changedCount, changedDetails);
        }
    }
    
    @Scheduled(fixedRate = 3600000)
    public void scanMaintenanceStatus() {
        List<String> changedDetails = new ArrayList<>();
        int changedCount = 0;
        int totalScanned = 0;
        
        for (ParkingSpace s : storage.parkingSpaces.values()) {
            if ("MAINTENANCE".equals(s.getStatus())) {
                totalScanned++;
                if (s.getOccupiedTime() != null) {
                    long days = Duration.between(s.getOccupiedTime(), LocalDateTime.now()).toDays();
                    if (days > 7) {
                        changedDetails.add("车位[" + s.getSpaceNo() + "] 维修状态已超过7天，建议检查");
                        changedCount++;
                    }
                }
            }
        }
        
        if (changedCount > 0 || totalScanned > 0) {
            createScanLog("维修状态扫描", totalScanned, changedCount, changedDetails);
        }
    }
    
    @Scheduled(fixedRate = 60000)
    public void updateCardStatus() {
        int cardCount = storage.monthlyCards.size();
        List<String> changedDetails = new ArrayList<>();
        int changedCount = 0;
        LocalDateTime now = LocalDateTime.now();
        
        for (com.parking.entity.MonthlyCard card : storage.monthlyCards.values()) {
            if ("ACTIVE".equals(card.getStatus()) && card.getEndDate() != null) {
                if (card.getEndDate().isBefore(now.toLocalDate())) {
                    card.setStatus("EXPIRED");
                    changedDetails.add("月卡[" + card.getPlateNumber() + "] 已过期");
                    changedCount++;
                }
            }
        }
        
        if (changedCount > 0 || cardCount > 0) {
            createScanLog("月卡状态扫描", cardCount, changedCount, changedDetails);
        }
    }
    
    private void createScanLog(String scanType, int totalScanned, int changedCount, List<String> changedDetails) {
        ScanLog log = new ScanLog();
        log.setId(UUID.randomUUID().toString());
        log.setScanType(scanType);
        log.setScanTime(LocalDateTime.now());
        log.setTotalScanned(totalScanned);
        log.setChangedCount(changedCount);
        log.setChangedDetails(changedDetails);
        storage.scanLogs.put(log.getId(), log);
    }
}
