package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.HealthCheck;

@Repository
public class HealthCheckRepository {
    private static HashMap<UUID, HealthCheck> healthChecks = new HashMap<>();

    // Create
    public void createHealthCheck(HealthCheck healthCheck) {
        healthCheck.id = UUID.randomUUID();
        healthChecks.put(healthCheck.id, healthCheck);
    }

    // Read
    public List<HealthCheck> findHealthChecksByDonorId(UUID donorId) {
        List<HealthCheck> checks = new ArrayList<>();
        for (HealthCheck check : healthChecks.values()) {
            if (check.donorId.equals(donorId)) {
                checks.add(check);
            }
        }
        return checks;
    }

    public List<HealthCheck> listAllHealthChecks() {
        return new ArrayList<>(healthChecks.values());
    }

    // Update
    public void updateHealthCheck(UUID healthCheckId, HealthCheck updatedHealthCheck) {
        HealthCheck check = healthChecks.get(healthCheckId);
        if (check != null) {
            check.dateTaken = updatedHealthCheck.dateTaken;
            check.doctorName = updatedHealthCheck.doctorName;
            check.measuredIronLevel = updatedHealthCheck.measuredIronLevel;
            check.checkPassed = updatedHealthCheck.checkPassed;
        }
    }

    // Delete
    public void deleteHealthCheck(UUID healthCheckId) {
        healthChecks.remove(healthCheckId);
    }

    public void clear() {
        healthChecks.clear();
    }

}
