package com.traffic;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidVehicleException;
import com.traffic.model.Challan;
import com.traffic.model.ViolationType;
import com.traffic.service.TrafficManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TrafficManagementServiceTest {
    private TrafficManagementService service;

    @BeforeEach
    void setUp() {
        service = new TrafficManagementService();
        service.registerVehicle("TN01AB1234", "John Doe");
    }

    @Test
    void testNormalChallanGeneration() {
        Challan c = service.generateChallan("TN01AB1234", ViolationType.ILLEGAL_PARKING, "Main Street", LocalDateTime.now(), 0, 0);
        assertNotNull(c);
        assertEquals(500.0, c.getFineAmount());
    }

    @Test
    void testInvalidVehicleRegistration() {
        assertThrows(InvalidVehicleException.class, () -> service.registerVehicle("", "Jane Doe"));
    }

    @Test
    void testDuplicateChallanPrevention() {
        LocalDateTime time = LocalDateTime.now();
        service.generateChallan("TN01AB1234", ViolationType.SIGNAL_VIOLATION, "Crossroad", time, 0, 0);
        assertThrows(DuplicateChallanException.class, () -> 
            service.generateChallan("TN01AB1234", ViolationType.SIGNAL_VIOLATION, "Crossroad", time, 0, 0)
        );
    }

    @Test
    void testRepeatViolationPenalty() {
        LocalDateTime time1 = LocalDateTime.now().minusDays(2);
        LocalDateTime time2 = LocalDateTime.now().minusDays(1);
        service.generateChallan("TN01AB1234", ViolationType.ILLEGAL_PARKING, "Street A", time1, 0, 0);
        Challan secondChallan = service.generateChallan("TN01AB1234", ViolationType.ILLEGAL_PARKING, "Street B", time2, 0, 0);
        
        assertEquals(750.0, secondChallan.getFineAmount());
    }
}