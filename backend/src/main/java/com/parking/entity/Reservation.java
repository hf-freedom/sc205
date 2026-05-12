package com.parking.entity;

import java.time.LocalDateTime;

public class Reservation {
    private String id;
    private String plateNumber;
    private String vehicleType;
    private String parkingSpaceId;
    private LocalDateTime reservationTime;
    private LocalDateTime expectedEntryTime;
    private LocalDateTime expiredTime;
    private LocalDateTime actualEntryTime;
    private String status;
    private String contactPhone;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(String parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }

    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }

    public LocalDateTime getExpectedEntryTime() { return expectedEntryTime; }
    public void setExpectedEntryTime(LocalDateTime expectedEntryTime) { this.expectedEntryTime = expectedEntryTime; }

    public LocalDateTime getExpiredTime() { return expiredTime; }
    public void setExpiredTime(LocalDateTime expiredTime) { this.expiredTime = expiredTime; }

    public LocalDateTime getActualEntryTime() { return actualEntryTime; }
    public void setActualEntryTime(LocalDateTime actualEntryTime) { this.actualEntryTime = actualEntryTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}
