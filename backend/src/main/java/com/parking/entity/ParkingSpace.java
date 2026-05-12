package com.parking.entity;

import java.time.LocalDateTime;

public class ParkingSpace {
    private String id;
    private String spaceNo;
    private String spaceType;
    private String status;
    private String plateNumber;
    private LocalDateTime occupiedTime;
    private Long occupiedDuration;
    private String zone;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSpaceNo() { return spaceNo; }
    public void setSpaceNo(String spaceNo) { this.spaceNo = spaceNo; }

    public String getSpaceType() { return spaceType; }
    public void setSpaceType(String spaceType) { this.spaceType = spaceType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public LocalDateTime getOccupiedTime() { return occupiedTime; }
    public void setOccupiedTime(LocalDateTime occupiedTime) { this.occupiedTime = occupiedTime; }

    public Long getOccupiedDuration() { return occupiedDuration; }
    public void setOccupiedDuration(Long occupiedDuration) { this.occupiedDuration = occupiedDuration; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}
