package lsit.Controllers;

import lsit.Models.BloodSample;
import lsit.Repositories.BloodSampleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blood-samples")
public class BloodSampleController {

    private final BloodSampleRepository bloodSampleRepository = new BloodSampleRepository();

    // Create
    @PostMapping("")
    public ResponseEntity<BloodSample> createBloodSample(@RequestBody BloodSample sample) {
        bloodSampleRepository.createBloodSample(sample);
        return ResponseEntity.ok(sample); // Return the created blood sample
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<BloodSample> getBloodSample(@PathVariable UUID id) {
        BloodSample sample = bloodSampleRepository.findBloodSampleById(id);
        return sample != null ? ResponseEntity.ok(sample) : ResponseEntity.notFound().build();
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<BloodSample>> listBloodSamplesByDonor(@PathVariable UUID donorId) {
        List<BloodSample> samples = bloodSampleRepository.listBloodSamplesByDonor(donorId);
        if (samples.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no samples found
        }
        return ResponseEntity.ok(samples);
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllBloodSamples() {
        List<BloodSample> samples = bloodSampleRepository.listAllBloodSamples();
        if (samples.isEmpty()) {
            // Return 200 status with a custom message in the response body
            return ResponseEntity.ok(Collections.singletonMap("message", "No blood samples currently stored."));
        }
        return ResponseEntity.ok(samples);
    }

    // Update
    @PutMapping("/{id}/quality")
    public ResponseEntity<BloodSample> updateQualityStatus(@PathVariable UUID id,
            @RequestParam BloodSample.QualityStatus qualityStatus) {
        BloodSample sample = bloodSampleRepository.findBloodSampleById(id);
        if (sample == null) {
            return ResponseEntity.notFound().build(); // Return 404 if sample not found
        }
        bloodSampleRepository.updateQualityStatus(id, qualityStatus);
        sample.qualityStatus = qualityStatus; // Update the sample object locally
        return ResponseEntity.ok(sample); // Return the updated sample
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<BloodSample> updateProgressStatus(@PathVariable UUID id,
            @RequestParam BloodSample.ProgressStatus progressStatus) {
        BloodSample sample = bloodSampleRepository.findBloodSampleById(id);
        if (sample == null) {
            return ResponseEntity.notFound().build(); // Return 404 if sample not found
        }
        bloodSampleRepository.updateProgressStatus(id, progressStatus);
        sample.progressStatus = progressStatus; // Update the sample object locally
        return ResponseEntity.ok(sample); // Return the updated sample
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodSample(@PathVariable UUID id) {
        BloodSample sample = bloodSampleRepository.findBloodSampleById(id);
        if (sample == null) {
            return ResponseEntity.notFound().build(); // Return 404 if sample not found
        }
        bloodSampleRepository.deleteBloodSample(id);
        return ResponseEntity.noContent().build(); // Return 204 with no content
    }
}
