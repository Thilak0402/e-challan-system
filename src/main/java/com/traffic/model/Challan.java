package com.traffic.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Challan {
    private String challanId;
    private String vehicleNumber;
    private ViolationType violationType;
    private String location;
    private LocalDateTime timestamp;
    private double fineAmount;
    private PaymentStatus paymentStatus;

    public Challan(String challanId, String vehicleNumber, ViolationType violationType, String location, LocalDateTime timestamp, double fineAmount) {
        this.challanId = challanId;
        this.vehicleNumber = vehicleNumber;
        this.violationType = violationType;
        this.location = location;
        this.timestamp = timestamp;
        this.fineAmount = fineAmount;
        this.paymentStatus = PaymentStatus.UNPAID;
    }

    public String getChallanId() { return challanId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public ViolationType getViolationType() { return violationType; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public double getFineAmount() { return fineAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Challan challan = (Challan) o;
        return vehicleNumber.equals(challan.vehicleNumber) &&
                violationType == challan.violationType &&
                timestamp.equals(challan.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleNumber, violationType, timestamp);
    }
}