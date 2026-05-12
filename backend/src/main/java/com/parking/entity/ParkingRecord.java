package com.parking.entity;

import java.time.LocalDateTime;

public class ParkingRecord {
    private String id;
    private String plateNumber;
    private String vehicleType;
    private String parkingSpaceId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long durationMinutes;
    private Double totalFee;
    private Double actualFee;
    private String billingType;
    private String status;
    private String remark;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getParkingSpaceId() { return parkingSpaceId; }
    public void setParkingSpaceId(String parkingSpaceId) { this.parkingSpaceId = parkingSpaceId; }

    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }

    public Long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Long durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getTotalFee() { return totalFee; }
    public void setTotalFee(Double totalFee) { this.totalFee = totalFee; }

    public Double getActualFee() { return actualFee; }
    public void setActualFee(Double actualFee) { this.actualFee = actualFee; }

    public String getBillingType() { return billingType; }
    public void setBillingType(String billingType) { this.billingType = billingType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
