package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.BloodSample;

@Repository
public class BloodSampleRepository {
    private static HashMap<UUID, BloodSample> bloodSamples = new HashMap<>();

    // Create
    public void createBloodSample(BloodSample sample) {
        sample.id = UUID.randomUUID();
        bloodSamples.put(sample.id, sample);
    }

    // Read
    public BloodSample findBloodSampleById(UUID sampleId) {
        return bloodSamples.get(sampleId);
    }

    public List<BloodSample> listBloodSamplesByDonor(UUID donorId) {
        List<BloodSample> samples = new ArrayList<>();
        for (BloodSample sample : bloodSamples.values()) {
            if (sample.donorId.equals(donorId)) {
                samples.add(sample);
            }
        }
        return samples;
    }

    public List<BloodSample> listAllBloodSamples() {
        return new ArrayList<>(bloodSamples.values());
    }

    // Update
    public void updateQualityStatus(UUID sampleId, BloodSample.QualityStatus qualityStatus) {
        BloodSample sample = bloodSamples.get(sampleId);
        if (sample != null) {
            sample.qualityStatus = qualityStatus;
        }
    }

    public void updateProgressStatus(UUID sampleId, BloodSample.ProgressStatus progressStatus) {
        BloodSample sample = bloodSamples.get(sampleId);
        if (sample != null) {
            sample.progressStatus = progressStatus;
        }
    }

    // Delete
    public void deleteBloodSample(UUID sampleId) {
        bloodSamples.remove(sampleId);
    }

    public void clear() {
        bloodSamples.clear();
    }

}
