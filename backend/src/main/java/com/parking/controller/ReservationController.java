package com.parking.controller;

import com.parking.entity.Reservation;
import com.parking.entity.Result;
import com.parking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        }
        try {
            if (dateTimeStr.endsWith("Z")) {
                dateTimeStr = dateTimeStr.substring(0, dateTimeStr.length() - 1);
            }
            if (dateTimeStr.contains(".")) {
                dateTimeStr = dateTimeStr.substring(0, dateTimeStr.indexOf("."));
            }
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }
    
    @PostMapping("/create")
    public Result<?> createReservation(@RequestBody Map<String, Object> params) {
        String plateNumber = (String) params.get("plateNumber");
        String vehicleType = (String) params.get("vehicleType");
        if (vehicleType == null) {
            vehicleType = "NORMAL";
        }
        String parkingSpaceId = (String) params.get("parkingSpaceId");
        String expectedEntryTimeStr = (String) params.get("expectedEntryTime");
        LocalDateTime expectedEntryTime = parseDateTime(expectedEntryTimeStr);
        
        if (expectedEntryTime == null) {
            return Result.error("日期格式错误");
        }
        
        String contactPhone = (String) params.get("contactPhone");
        
        Reservation reservation = reservationService.createReservation(
                plateNumber, vehicleType, parkingSpaceId, expectedEntryTime, contactPhone);
        
        return reservation != null ? Result.success(reservation) : Result.error("预约失败");
    }
    
    @PostMapping("/cancel")
    public Result<?> cancelReservation(@RequestBody Map<String, String> params) {
        String reservationId = params.get("reservationId");
        Reservation reservation = reservationService.cancelReservation(reservationId);
        return reservation != null ? Result.success(reservation) : Result.error("取消失败");
    }
    
    @GetMapping("/list")
    public Result<List<Reservation>> getAllReservations() {
        return Result.success(reservationService.getAllReservations());
    }
    
    @GetMapping("/active")
    public Result<List<Reservation>> getActiveReservations() {
        return Result.success(reservationService.getActiveReservations());
    }
}
