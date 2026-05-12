package com.parking.controller;

import com.parking.entity.MonthlyCard;
import com.parking.entity.Result;
import com.parking.service.MonthlyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monthly-card")
public class MonthlyCardController {
    
    @Autowired
    private MonthlyCardService monthlyCardService;
    
    @PostMapping("/create")
    public Result<MonthlyCard> createMonthlyCard(@RequestBody MonthlyCard card) {
        return Result.success(monthlyCardService.createMonthlyCard(card));
    }
    
    @PostMapping("/renew")
    public Result<?> renewMonthlyCard(@RequestBody Map<String, Object> params) {
        String cardId = (String) params.get("cardId");
        int months = Integer.parseInt(params.get("months").toString());
        MonthlyCard card = monthlyCardService.renewMonthlyCard(cardId, months);
        return card != null ? Result.success(card) : Result.error("月卡不存在");
    }
    
    @PostMapping("/cancel")
    public Result<?> cancelMonthlyCard(@RequestBody Map<String, String> params) {
        String cardId = params.get("cardId");
        MonthlyCard card = monthlyCardService.cancelMonthlyCard(cardId);
        return card != null ? Result.success(card) : Result.error("月卡不存在");
    }
    
    @GetMapping("/list")
    public Result<List<MonthlyCard>> getAllMonthlyCards() {
        return Result.success(monthlyCardService.getAllMonthlyCards());
    }
    
    @GetMapping("/plate/{plateNumber}")
    public Result<?> getByPlateNumber(@PathVariable String plateNumber) {
        return monthlyCardService.getMonthlyCardByPlateNumber(plateNumber)
                .map(Result::success)
                .orElseGet(() -> Result.error("未找到月卡"));
    }
}
