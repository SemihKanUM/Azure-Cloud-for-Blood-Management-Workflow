package test.java.lsit.Models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckTest {

    @Test
    void testHealthCheckInitialization() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID donorId = UUID.randomUUID();
        LocalDate dateTaken = LocalDate.of(2024, 11, 29);
        String doctorName = "Dr. Smith";
        float measuredIronLevel = 12.5f;
        boolean checkPassed = true;

        // Act
        HealthCheck healthCheck = new HealthCheck();
        healthCheck.id = id;
        healthCheck.donorId = donorId;
        healthCheck.dateTaken = dateTaken;
        healthCheck.doctorName = doctorName;
        healthCheck.measuredIronLevel = measuredIronLevel;
        healthCheck.checkPassed = checkPassed;

        // Assert
        assertEquals(id, healthCheck.id);
        assertEquals(donorId, healthCheck.donorId);
        assertEquals(dateTaken, healthCheck.dateTaken);
        assertEquals(doctorName, healthCheck.doctorName);
        assertEquals(measuredIronLevel, healthCheck.measuredIronLevel);
        assertTrue(healthCheck.checkPassed);
    }

    @Test
    void testDefaultValues() {
        // Act
        HealthCheck healthCheck = new HealthCheck();

        // Assert
        assertNull(healthCheck.id);
        assertNull(healthCheck.donorId);
        assertNull(healthCheck.dateTaken);
        assertNull(healthCheck.doctorName);
        assertEquals(0.0f, healthCheck.measuredIronLevel);
        assertFalse(healthCheck.checkPassed);
    }
}

