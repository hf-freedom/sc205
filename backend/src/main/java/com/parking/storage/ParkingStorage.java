package com.parking.storage;

import com.parking.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ParkingStorage {
    
    public final Map<String, ParkingSpace> parkingSpaces = new ConcurrentHashMap<>();
    public final Map<String, ParkingRecord> parkingRecords = new ConcurrentHashMap<>();
    public final Map<String, MonthlyCard> monthlyCards = new ConcurrentHashMap<>();
    public final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    public final Map<String, AbnormalRelease> abnormalReleases = new ConcurrentHashMap<>();
    public final Map<String, ScanLog> scanLogs = new ConcurrentHashMap<>();
    
    public ParkingStorage() {
        initData();
    }
    
    private void initData() {
        for (int i = 1; i <= 30; i++) {
            ParkingSpace space = new ParkingSpace();
            String spaceId = "P" + String.format("%03d", i);
            space.setId(spaceId);
            space.setSpaceNo(spaceId);
            
            if (i <= 10) {
                space.setSpaceType("MONTHLY");
                space.setZone("月卡区");
            } else if (i <= 20) {
                space.setSpaceType("TEMPORARY");
                space.setZone("临停区");
            } else {
                space.setSpaceType("NORMAL");
                space.setZone("普通区");
            }
            
            space.setStatus("AVAILABLE");
            parkingSpaces.put(spaceId, space);
        }
        
        MonthlyCard card1 = new MonthlyCard();
        card1.setId(UUID.randomUUID().toString());
        card1.setPlateNumber("京A12345");
        card1.setOwnerName("张三");
        card1.setOwnerPhone("13800138001");
        card1.setStartDate(LocalDate.now().minusMonths(1));
        card1.setEndDate(LocalDate.now().plusMonths(1));
        card1.setStatus("ACTIVE");
        card1.setMonthlyFee(300.0);
        card1.setCreateTime(LocalDateTime.now().minusMonths(1));
        monthlyCards.put(card1.getId(), card1);
        
        MonthlyCard card2 = new MonthlyCard();
        card2.setId(UUID.randomUUID().toString());
        card2.setPlateNumber("京B67890");
        card2.setOwnerName("李四");
        card2.setOwnerPhone("13800138002");
        card2.setStartDate(LocalDate.now().minusDays(5));
        card2.setEndDate(LocalDate.now().minusDays(1));
        card2.setStatus("EXPIRED");
        card2.setMonthlyFee(300.0);
        card2.setCreateTime(LocalDateTime.now().minusDays(5));
        monthlyCards.put(card2.getId(), card2);
    }
}
