package lsit.Controllers;

import lsit.Models.HealthCheck;
import lsit.Repositories.HealthCheckRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/health-checks")
public class HealthCheckController {

    private final HealthCheckRepository healthCheckRepository = new HealthCheckRepository();

    // Create
    @PostMapping("")
    public ResponseEntity<HealthCheck> createHealthCheck(@RequestBody HealthCheck healthCheck) {
        healthCheckRepository.createHealthCheck(healthCheck);
        return ResponseEntity.ok(healthCheck); // Return the created health check
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<HealthCheck> getHealthCheck(@PathVariable UUID id) {
        HealthCheck healthCheck = healthCheckRepository.listAllHealthChecks()
                .stream()
                .filter(check -> check.id.equals(id))
                .findFirst()
                .orElse(null);
        return healthCheck != null ? ResponseEntity.ok(healthCheck) : ResponseEntity.notFound().build();
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<HealthCheck>> listHealthChecksByDonor(@PathVariable UUID donorId) {
        List<HealthCheck> healthChecks = healthCheckRepository.findHealthChecksByDonorId(donorId);
        if (healthChecks.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no health checks found
        }
        return ResponseEntity.ok(healthChecks);
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllHealthChecks() {
        List<HealthCheck> healthChecks = healthCheckRepository.listAllHealthChecks();
        if (healthChecks.isEmpty()) {
            // Return 200 status with a custom message in the response body
            return ResponseEntity.ok(Collections.singletonMap("message", "No health checks currently stored."));
        }
        return ResponseEntity.ok(healthChecks);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<HealthCheck> updateHealthCheck(@PathVariable UUID id,
            @RequestBody HealthCheck updatedHealthCheck) {
        HealthCheck existingHealthCheck = healthCheckRepository.listAllHealthChecks()
                .stream()
                .filter(check -> check.id.equals(id))
                .findFirst()
                .orElse(null);
        if (existingHealthCheck == null) {
            return ResponseEntity.notFound().build(); // Return 404 if health check not found
        }
        healthCheckRepository.updateHealthCheck(id, updatedHealthCheck);
        return ResponseEntity.ok(updatedHealthCheck); // Return the updated health check
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthCheck(@PathVariable UUID id) {
        HealthCheck existingHealthCheck = healthCheckRepository.listAllHealthChecks()
                .stream()
                .filter(check -> check.id.equals(id))
                .findFirst()
                .orElse(null);
        if (existingHealthCheck == null) {
            return ResponseEntity.notFound().build(); // Return 404 if health check not found
        }
        healthCheckRepository.deleteHealthCheck(id);
        return ResponseEntity.noContent().build(); // Return 204 with no content
    }
}
