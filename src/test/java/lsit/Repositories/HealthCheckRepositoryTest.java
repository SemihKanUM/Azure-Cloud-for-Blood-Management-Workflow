package test.java.lsit.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lsit.Models.HealthCheck;
import lsit.Repositories.HealthCheckRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HealthCheckRepositoryTest {

    private HealthCheckRepository healthCheckRepository;
    private HealthCheck testHealthCheck;
    private UUID donorId;

    @BeforeEach
    void setUp() {
        healthCheckRepository = new HealthCheckRepository();

        // Set up a test donorId for the health check
        donorId = UUID.randomUUID();

        // Create and populate the test HealthCheck
        testHealthCheck = new HealthCheck();
        testHealthCheck.id = UUID.randomUUID();
        testHealthCheck.donorId = donorId;
        testHealthCheck.dateTaken = "2024-11-01";
        testHealthCheck.doctorName = "Dr. Smith";
        testHealthCheck.measuredIronLevel = 110;
        testHealthCheck.checkPassed = true;

        healthCheckRepository.clear(); // Clear the repository before each test
    }

    @Test
    void createHealthCheck_ShouldAddHealthCheckToRepository() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        HealthCheck retrievedHealthCheck = healthCheckRepository.findHealthChecksByDonorId(donorId).get(0);
        assertNotNull(retrievedHealthCheck);
        assertEquals(testHealthCheck.id, retrievedHealthCheck.id);
        assertEquals(testHealthCheck.donorId, retrievedHealthCheck.donorId);
        assertEquals(testHealthCheck.dateTaken, retrievedHealthCheck.dateTaken);
    }

    @Test
    void findHealthChecksByDonorId_ShouldReturnCorrectHealthChecks() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        HealthCheck secondHealthCheck = new HealthCheck();
        secondHealthCheck.id = UUID.randomUUID();
        secondHealthCheck.donorId = donorId;
        secondHealthCheck.dateTaken = "2024-11-02";
        secondHealthCheck.doctorName = "Dr. Jones";
        secondHealthCheck.measuredIronLevel = 120;
        secondHealthCheck.checkPassed = false;
        healthCheckRepository.createHealthCheck(secondHealthCheck);

        List<HealthCheck> healthChecks = healthCheckRepository.findHealthChecksByDonorId(donorId);
        assertNotNull(healthChecks);
        assertEquals(2, healthChecks.size());
        assertTrue(healthChecks.contains(testHealthCheck));
        assertTrue(healthChecks.contains(secondHealthCheck));
    }

    @Test
    void findHealthChecksByDonorId_WhenNoChecks_ShouldReturnEmptyList() {
        List<HealthCheck> healthChecks = healthCheckRepository.findHealthChecksByDonorId(UUID.randomUUID());
        assertNotNull(healthChecks);
        assertTrue(healthChecks.isEmpty());
    }

    @Test
    void listAllHealthChecks_ShouldReturnAllHealthChecks() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        HealthCheck secondHealthCheck = new HealthCheck();
        secondHealthCheck.id = UUID.randomUUID();
        secondHealthCheck.donorId = UUID.randomUUID();
        secondHealthCheck.dateTaken = "2024-11-02";
        secondHealthCheck.doctorName = "Dr. Adams";
        secondHealthCheck.measuredIronLevel = 130;
        secondHealthCheck.checkPassed = true;
        healthCheckRepository.createHealthCheck(secondHealthCheck);

        List<HealthCheck> allHealthChecks = healthCheckRepository.listAllHealthChecks();
        assertNotNull(allHealthChecks);
        assertEquals(2, allHealthChecks.size());
    }

    @Test
    void updateHealthCheck_ShouldUpdateHealthCheckDetails() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        HealthCheck updatedHealthCheck = new HealthCheck();
        updatedHealthCheck.id = testHealthCheck.id;
        updatedHealthCheck.donorId = testHealthCheck.donorId;
        updatedHealthCheck.dateTaken = "2024-11-05";
        updatedHealthCheck.doctorName = "Dr. Johnson";
        updatedHealthCheck.measuredIronLevel = 115;
        updatedHealthCheck.checkPassed = false;

        healthCheckRepository.updateHealthCheck(testHealthCheck.id, updatedHealthCheck);

        HealthCheck retrievedHealthCheck = healthCheckRepository.findHealthChecksByDonorId(donorId).get(0);
        assertNotNull(retrievedHealthCheck);
        assertEquals("2024-11-05", retrievedHealthCheck.dateTaken);
        assertEquals("Dr. Johnson", retrievedHealthCheck.doctorName);
        assertEquals(115, retrievedHealthCheck.measuredIronLevel);
        assertFalse(retrievedHealthCheck.checkPassed);
    }

    @Test
    void deleteHealthCheck_ShouldRemoveHealthCheckFromRepository() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        healthCheckRepository.deleteHealthCheck(testHealthCheck.id);

        List<HealthCheck> healthChecks = healthCheckRepository.findHealthChecksByDonorId(donorId);
        assertTrue(healthChecks.isEmpty());
    }

    @Test
    void clear_ShouldRemoveAllHealthChecksFromRepository() {
        healthCheckRepository.createHealthCheck(testHealthCheck);

        healthCheckRepository.clear();

        List<HealthCheck> healthChecks = healthCheckRepository.listAllHealthChecks();
        assertTrue(healthChecks.isEmpty());
    }
}

