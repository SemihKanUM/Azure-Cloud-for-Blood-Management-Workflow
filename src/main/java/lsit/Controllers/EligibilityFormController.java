package lsit.Controllers;

import lsit.Models.EligibilityForm;
import lsit.Repositories.EligibilityFormRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eligibility-forms")
public class EligibilityFormController {

    private final EligibilityFormRepository eligibilityFormRepository = new EligibilityFormRepository();

    // Create
    @PostMapping("")
    public ResponseEntity<EligibilityForm> createEligibilityForm(@RequestBody EligibilityForm form) {
        eligibilityFormRepository.createEligibilityForm(form);
        return ResponseEntity.ok(form); // Return the created form as a machine-readable response
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<EligibilityForm> getEligibilityForm(@PathVariable UUID id) {
        EligibilityForm form = eligibilityFormRepository.listAllEligibilityForms()
                .stream()
                .filter(f -> f.id.equals(id))
                .findFirst()
                .orElse(null);
        return form != null ? ResponseEntity.ok(form) : ResponseEntity.notFound().build();
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<EligibilityForm>> listEligibilityFormsByDonor(@PathVariable UUID donorId) {
        List<EligibilityForm> forms = eligibilityFormRepository.findEligibilityFormsByDonorId(donorId);
        if (forms.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no forms found
        }
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllEligibilityForms() {
        List<EligibilityForm> forms = eligibilityFormRepository.listAllEligibilityForms();
        if (forms.isEmpty()) {
            // Return 200 status with a custom message in the response body
            return ResponseEntity.ok(Collections.singletonMap("message", "No eligibility forms currently stored."));
        }
        return ResponseEntity.ok(forms);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<EligibilityForm> updateEligibilityForm(@PathVariable UUID id,
            @RequestBody EligibilityForm updatedForm) {
        EligibilityForm existingForm = eligibilityFormRepository.listAllEligibilityForms()
                .stream()
                .filter(f -> f.id.equals(id))
                .findFirst()
                .orElse(null);
        if (existingForm == null) {
            return ResponseEntity.notFound().build(); // Return 404 if the form does not exist
        }
        eligibilityFormRepository.updateEligibilityForm(id, updatedForm);
        return ResponseEntity.ok(updatedForm); // Return the updated form as a response
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEligibilityForm(@PathVariable UUID id) {
        EligibilityForm existingForm = eligibilityFormRepository.listAllEligibilityForms()
                .stream()
                .filter(f -> f.id.equals(id))
                .findFirst()
                .orElse(null);
        if (existingForm == null) {
            return ResponseEntity.notFound().build(); // Return 404 if the form does not exist
        }
        eligibilityFormRepository.deleteEligibilityForm(id);
        return ResponseEntity.noContent().build(); // Return 204 with no content
    }
}
