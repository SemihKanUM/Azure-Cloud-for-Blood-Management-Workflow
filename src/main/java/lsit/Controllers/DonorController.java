package lsit.Controllers;

import lsit.Models.Donor;
import lsit.Repositories.DonorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/donors")
public class DonorController {

    private final DonorRepository donorRepository = new DonorRepository();

    // Create
    @PostMapping("")
    public ResponseEntity<Donor> createDonor(@RequestBody Donor donor) {
        donorRepository.createDonor(donor);
        return ResponseEntity.ok(donor); // Return the created donor as a response
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorDetails(@PathVariable UUID id) {
        Donor donor = donorRepository.findDonorById(id);
        return donor != null ? ResponseEntity.ok(donor) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllDonors() {
        List<Donor> donors = donorRepository.listAllDonors();
        if (donors.isEmpty()) {
             // Return 200 status with a custom message in the response body
            return ResponseEntity.ok(Collections.singletonMap("message", "No donors currently stored."));
        }
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{id}/health-status")
    public ResponseEntity<Boolean> getDonorHealthStatus(@PathVariable UUID id) {
        Donor donor = donorRepository.findDonorById(id);
        return donor != null ? ResponseEntity.ok(donor.healthCheck) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/eligibility-status")
    public ResponseEntity<Boolean> getDonorEligibilityStatus(@PathVariable UUID id) {
        Donor donor = donorRepository.findDonorById(id);
        return donor != null ? ResponseEntity.ok(donor.eligibilityForm) : ResponseEntity.notFound().build();
    }

    // Update
    @PutMapping("/{id}/eligibility")
    public ResponseEntity<Donor> updateDonorEligibilityFormStatus(@PathVariable UUID id,
            @RequestParam boolean eligible) {
        Donor donor = donorRepository.findDonorById(id);
        if (donor == null) {
            return ResponseEntity.notFound().build(); // Return 404 if donor not found
        }
        donorRepository.updateDonorEligibilityFormStatus(id, eligible);
        donor.eligibilityForm = eligible;
        return ResponseEntity.ok(donor); // Return updated donor object
    }

    @PutMapping("/{id}/health-check")
    public ResponseEntity<Donor> updateDonorHealthCheckStatus(@PathVariable UUID id, @RequestParam boolean healthy) {
        Donor donor = donorRepository.findDonorById(id);
        if (donor == null) {
            return ResponseEntity.notFound().build(); // Return 404 if donor not found
        }
        donorRepository.updateDonorHealthCheckStatus(id, healthy);
        donor.healthCheck = healthy;
        return ResponseEntity.ok(donor); // Return updated donor object
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonorDetails(@PathVariable UUID id, @RequestBody Donor updatedDonor) {
        Donor donor = donorRepository.findDonorById(id);
        if (donor == null) {
            return ResponseEntity.notFound().build(); // Return 404 if donor not found
        }
        donorRepository.updateDonorDetails(id, updatedDonor);
        return ResponseEntity.ok(updatedDonor); // Return updated donor object
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonor(@PathVariable UUID id) {
        Donor donor = donorRepository.findDonorById(id);
        if (donor == null) {
            return ResponseEntity.notFound().build(); // Return 404 if donor not found
        }
        donorRepository.deleteDonor(id);
        return ResponseEntity.noContent().build(); // Return 204 with no content
    }
}
