package com.parking.service;

import com.parking.entity.ScanLog;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanLogService {
    
    @Autowired
    private ParkingStorage storage;
    
    public List<ScanLog> getAllScanLogs() {
        return storage.scanLogs.values().stream()
                .sorted(Comparator.comparing(ScanLog::getScanTime).reversed())
                .collect(Collectors.toList());
    }
    
    public List<ScanLog> getScanLogsByType(String scanType) {
        return storage.scanLogs.values().stream()
                .filter(log -> scanType.equals(log.getScanType()))
                .sorted(Comparator.comparing(ScanLog::getScanTime).reversed())
                .collect(Collectors.toList());
    }
    
    public List<ScanLog> getRecentScanLogs(int limit) {
        return storage.scanLogs.values().stream()
                .sorted(Comparator.comparing(ScanLog::getScanTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
