package com.parking.service;

import com.parking.entity.MonthlyCard;
import com.parking.storage.ParkingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonthlyCardService {
    
    @Autowired
    private ParkingStorage storage;
    
    public MonthlyCard createMonthlyCard(MonthlyCard card) {
        card.setId(UUID.randomUUID().toString());
        card.setCreateTime(LocalDateTime.now());
        card.setStatus("ACTIVE");
        
        if (card.getStartDate() == null) {
            card.setStartDate(LocalDate.now());
        }
        if (card.getEndDate() == null) {
            card.setEndDate(card.getStartDate().plusMonths(1));
        }
        
        storage.monthlyCards.put(card.getId(), card);
        return card;
    }
    
    public MonthlyCard renewMonthlyCard(String cardId, int months) {
        MonthlyCard card = storage.monthlyCards.get(cardId);
        if (card == null) {
            return null;
        }
        
        LocalDate newEndDate;
        if (card.getEndDate().isBefore(LocalDate.now())) {
            newEndDate = LocalDate.now().plusMonths(months);
        } else {
            newEndDate = card.getEndDate().plusMonths(months);
        }
        
        card.setEndDate(newEndDate);
        card.setStatus("ACTIVE");
        return card;
    }
    
    public MonthlyCard cancelMonthlyCard(String cardId) {
        MonthlyCard card = storage.monthlyCards.get(cardId);
        if (card == null) {
            return null;
        }
        card.setStatus("CANCELLED");
        return card;
    }
    
    public List<MonthlyCard> getAllMonthlyCards() {
        return new ArrayList<>(storage.monthlyCards.values());
    }
    
    public Optional<MonthlyCard> getMonthlyCardByPlateNumber(String plateNumber) {
        return storage.monthlyCards.values().stream()
                .filter(card -> card.getPlateNumber().equals(plateNumber))
                .findFirst();
    }
    
    public void checkAndUpdateCardStatus() {
        LocalDate today = LocalDate.now();
        storage.monthlyCards.values().stream()
                .filter(card -> "ACTIVE".equals(card.getStatus()) && card.getEndDate().isBefore(today))
                .forEach(card -> card.setStatus("EXPIRED"));
    }
}
