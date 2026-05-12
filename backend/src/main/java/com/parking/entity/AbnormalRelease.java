package com.parking.entity;

import java.time.LocalDateTime;

public class AbnormalRelease {
    private String id;
    private String parkingRecordId;
    private String plateNumber;
    private LocalDateTime releaseTime;
    private String reason;
    private String operator;
    private Double waivedFee;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getParkingRecordId() { return parkingRecordId; }
    public void setParkingRecordId(String parkingRecordId) { this.parkingRecordId = parkingRecordId; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public LocalDateTime getReleaseTime() { return releaseTime; }
    public void setReleaseTime(LocalDateTime releaseTime) { this.releaseTime = releaseTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Double getWaivedFee() { return waivedFee; }
    public void setWaivedFee(Double waivedFee) { this.waivedFee = waivedFee; }
}
