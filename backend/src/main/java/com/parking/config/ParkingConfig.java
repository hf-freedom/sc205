package com.parking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "parking.config")
public class ParkingConfig {
    private Integer freeMinutes = 30;
    private Integer hourlyRate = 5;
    private Integer dailyCap = 50;
    private Integer monthlyCardPrice = 300;
    private Integer reservationTimeoutMinutes = 30;
    private Integer longStayThresholdHours = 24;

    public Integer getFreeMinutes() { return freeMinutes; }
    public void setFreeMinutes(Integer freeMinutes) { this.freeMinutes = freeMinutes; }

    public Integer getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Integer hourlyRate) { this.hourlyRate = hourlyRate; }

    public Integer getDailyCap() { return dailyCap; }
    public void setDailyCap(Integer dailyCap) { this.dailyCap = dailyCap; }

    public Integer getMonthlyCardPrice() { return monthlyCardPrice; }
    public void setMonthlyCardPrice(Integer monthlyCardPrice) { this.monthlyCardPrice = monthlyCardPrice; }

    public Integer getReservationTimeoutMinutes() { return reservationTimeoutMinutes; }
    public void setReservationTimeoutMinutes(Integer reservationTimeoutMinutes) { this.reservationTimeoutMinutes = reservationTimeoutMinutes; }

    public Integer getLongStayThresholdHours() { return longStayThresholdHours; }
    public void setLongStayThresholdHours(Integer longStayThresholdHours) { this.longStayThresholdHours = longStayThresholdHours; }
}
