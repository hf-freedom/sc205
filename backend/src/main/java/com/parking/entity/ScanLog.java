package com.parking.entity;

import java.time.LocalDateTime;
import java.util.List;

public class ScanLog {
    private String id;
    private String scanType;
    private LocalDateTime scanTime;
    private Integer totalScanned;
    private Integer changedCount;
    private List<String> changedDetails;
    private String remark;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }

    public LocalDateTime getScanTime() { return scanTime; }
    public void setScanTime(LocalDateTime scanTime) { this.scanTime = scanTime; }

    public Integer getTotalScanned() { return totalScanned; }
    public void setTotalScanned(Integer totalScanned) { this.totalScanned = totalScanned; }

    public Integer getChangedCount() { return changedCount; }
    public void setChangedCount(Integer changedCount) { this.changedCount = changedCount; }

    public List<String> getChangedDetails() { return changedDetails; }
    public void setChangedDetails(List<String> changedDetails) { this.changedDetails = changedDetails; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
