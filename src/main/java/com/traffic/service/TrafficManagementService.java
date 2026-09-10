package com.traffic.service;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidVehicleException;
import com.traffic.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class TrafficManagementService {
    private final Map<String, String> registeredVehicles = new HashMap<>();
    private final List<Challan> challans = new ArrayList<>();

    public void registerVehicle(String vehicleNumber, String ownerDetails) {
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty() || ownerDetails == null || ownerDetails.trim().isEmpty()) {
            throw new InvalidVehicleException("Invalid vehicle information provided.");
        }
        registeredVehicles.put(vehicleNumber, ownerDetails);
    }

    public Challan generateChallan(String vehicleNumber, ViolationType violationType, String location, LocalDateTime timestamp, int speed, int permittedSpeed) {
        if (!registeredVehicles.containsKey(vehicleNumber)) {
            throw new InvalidVehicleException("Vehicle not registered: " + vehicleNumber);
        }

        double baseFine = switch (violationType) {
            case OVER_SPEEDING -> (speed > permittedSpeed + 20) ? 2000.0 : 1000.0;
            case SIGNAL_VIOLATION -> 1500.0;
            case ILLEGAL_PARKING -> 500.0;
        };

        long pastViolationsCount = challans.stream()
                .filter(c -> c.getVehicleNumber().equals(vehicleNumber))
                .count();
        double finalFine = baseFine * (1 + (pastViolationsCount * 0.5));

        Challan newChallan = new Challan(UUID.randomUUID().toString(), vehicleNumber, violationType, location, timestamp, finalFine);

        if (challans.contains(newChallan)) {
            throw new DuplicateChallanException("Duplicate challan detected for the same event.");
        }

        challans.add(newChallan);
        return newChallan;
    }

    public void payChallan(String challanId) {
        Challan challan = challans.stream()
                .filter(c -> c.getChallanId().equals(challanId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Challan not found."));
        challan.setPaymentStatus(PaymentStatus.PAID);
    }

    public double calculateTotalOutstandingFines(String vehicleNumber) {
        return challans.stream()
                .filter(c -> c.getVehicleNumber().equals(vehicleNumber) && c.getPaymentStatus() == PaymentStatus.UNPAID)
                .mapToDouble(Challan::getFineAmount)
                .sum();
    }

    public String classifyVehicle(String vehicleNumber) {
        long count = challans.stream().filter(c -> c.getVehicleNumber().equals(vehicleNumber)).count();
        if (count >= 3) return "Habitual Offender";
        if (count > 0) return "Frequent Offender";
        return "Clean Record";
    }
}