package com.parking.entity;

public class Statistics {
    private Long totalSpaces;
    private Long availableSpaces;
    private Long occupiedSpaces;
    private Long lockedSpaces;
    private Long maintenanceSpaces;
    private Long reservedSpaces;
    private Double spaceUtilizationRate;
    private Double totalIncome;
    private Double temporaryParkingIncome;
    private Double monthlyCardIncome;
    private Long monthlyCardCount;
    private Long totalVehicles;
    private Double monthlyCardRatio;
    private Long abnormalReleaseCount;
    private Long currentParkingVehicles;

    public Long getTotalSpaces() { return totalSpaces; }
    public void setTotalSpaces(Long totalSpaces) { this.totalSpaces = totalSpaces; }

    public Long getAvailableSpaces() { return availableSpaces; }
    public void setAvailableSpaces(Long availableSpaces) { this.availableSpaces = availableSpaces; }

    public Long getOccupiedSpaces() { return occupiedSpaces; }
    public void setOccupiedSpaces(Long occupiedSpaces) { this.occupiedSpaces = occupiedSpaces; }

    public Long getLockedSpaces() { return lockedSpaces; }
    public void setLockedSpaces(Long lockedSpaces) { this.lockedSpaces = lockedSpaces; }

    public Long getMaintenanceSpaces() { return maintenanceSpaces; }
    public void setMaintenanceSpaces(Long maintenanceSpaces) { this.maintenanceSpaces = maintenanceSpaces; }

    public Long getReservedSpaces() { return reservedSpaces; }
    public void setReservedSpaces(Long reservedSpaces) { this.reservedSpaces = reservedSpaces; }

    public Double getSpaceUtilizationRate() { return spaceUtilizationRate; }
    public void setSpaceUtilizationRate(Double spaceUtilizationRate) { this.spaceUtilizationRate = spaceUtilizationRate; }

    public Double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(Double totalIncome) { this.totalIncome = totalIncome; }

    public Double getTemporaryParkingIncome() { return temporaryParkingIncome; }
    public void setTemporaryParkingIncome(Double temporaryParkingIncome) { this.temporaryParkingIncome = temporaryParkingIncome; }

    public Double getMonthlyCardIncome() { return monthlyCardIncome; }
    public void setMonthlyCardIncome(Double monthlyCardIncome) { this.monthlyCardIncome = monthlyCardIncome; }

    public Long getMonthlyCardCount() { return monthlyCardCount; }
    public void setMonthlyCardCount(Long monthlyCardCount) { this.monthlyCardCount = monthlyCardCount; }

    public Long getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(Long totalVehicles) { this.totalVehicles = totalVehicles; }

    public Double getMonthlyCardRatio() { return monthlyCardRatio; }
    public void setMonthlyCardRatio(Double monthlyCardRatio) { this.monthlyCardRatio = monthlyCardRatio; }

    public Long getAbnormalReleaseCount() { return abnormalReleaseCount; }
    public void setAbnormalReleaseCount(Long abnormalReleaseCount) { this.abnormalReleaseCount = abnormalReleaseCount; }

    public Long getCurrentParkingVehicles() { return currentParkingVehicles; }
    public void setCurrentParkingVehicles(Long currentParkingVehicles) { this.currentParkingVehicles = currentParkingVehicles; }
}
