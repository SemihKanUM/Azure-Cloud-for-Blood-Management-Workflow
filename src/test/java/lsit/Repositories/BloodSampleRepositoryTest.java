package test.java.lsit.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lsit.Models.BloodSample;
import lsit.Repositories.BloodSampleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BloodSampleRepositoryTest {

    @InjectMocks
    private BloodSampleRepository bloodSampleRepository;
    private BloodSample testSample;

    @BeforeEach
    void setUp() {
        bloodSampleRepository = new BloodSampleRepository();
        testSample = new BloodSample();
        testSample.id = UUID.randomUUID();
        testSample.donorId = UUID.randomUUID();
        testSample.collectionDate = LocalDate.now();
        testSample.qualityStatus = BloodSample.QualityStatus.UNTESTED;
        testSample.progressStatus = BloodSample.ProgressStatus.COLLECTED;

        bloodSampleRepository.clear(); // Clear the repository before each test
    }

    @Test
    void createBloodSample_ShouldAddSampleToRepository() {
        bloodSampleRepository.createBloodSample(testSample);

        BloodSample retrievedSample = bloodSampleRepository.findBloodSampleById(testSample.id);
        assertNotNull(retrievedSample);
        assertEquals(testSample.id, retrievedSample.id);
        assertEquals(testSample.donorId, retrievedSample.donorId);
    }

    @Test
    void findBloodSampleById_ShouldReturnCorrectSample() {
        bloodSampleRepository.createBloodSample(testSample);

        BloodSample retrievedSample = bloodSampleRepository.findBloodSampleById(testSample.id);
        assertNotNull(retrievedSample);
        assertEquals(testSample.id, retrievedSample.id);
        assertEquals(testSample.collectionDate, retrievedSample.collectionDate);
    }

    @Test
    void findBloodSampleById_WhenNotFound_ShouldReturnNull() {
        BloodSample retrievedSample = bloodSampleRepository.findBloodSampleById(UUID.randomUUID());
        assertNull(retrievedSample);
    }

    @Test
    void listBloodSamplesByDonor_ShouldReturnSamplesForGivenDonor() {
        bloodSampleRepository.createBloodSample(testSample);

        BloodSample secondSample = new BloodSample();
        secondSample.id = UUID.randomUUID();
        secondSample.donorId = testSample.donorId; // Same donor ID
        secondSample.collectionDate = LocalDate.now();
        bloodSampleRepository.createBloodSample(secondSample);

        List<BloodSample> samples = bloodSampleRepository.listBloodSamplesByDonor(testSample.donorId);
        assertNotNull(samples);
        assertEquals(2, samples.size());
        assertTrue(samples.contains(testSample));
        assertTrue(samples.contains(secondSample));
    }

    @Test
    void listBloodSamplesByDonor_WhenNoSamples_ShouldReturnEmptyList() {
        List<BloodSample> samples = bloodSampleRepository.listBloodSamplesByDonor(UUID.randomUUID());
        assertNotNull(samples);
        assertTrue(samples.isEmpty());
    }

    @Test
    void listAllBloodSamples_ShouldReturnAllSamples() {
        bloodSampleRepository.createBloodSample(testSample);

        BloodSample secondSample = new BloodSample();
        secondSample.id = UUID.randomUUID();
        secondSample.donorId = UUID.randomUUID();
        secondSample.collectionDate = LocalDate.now();
        bloodSampleRepository.createBloodSample(secondSample);

        List<BloodSample> samples = bloodSampleRepository.listAllBloodSamples();
        assertNotNull(samples);
        assertEquals(2, samples.size());
    }

    @Test
    void updateQualityStatus_ShouldUpdateSampleQualityStatus() {
        bloodSampleRepository.createBloodSample(testSample);

        bloodSampleRepository.updateQualityStatus(testSample.id, BloodSample.QualityStatus.APPROVED);

        BloodSample updatedSample = bloodSampleRepository.findBloodSampleById(testSample.id);
        assertNotNull(updatedSample);
        assertEquals(BloodSample.QualityStatus.APPROVED, updatedSample.qualityStatus);
    }

    @Test
    void updateProgressStatus_ShouldUpdateSampleProgressStatus() {
        bloodSampleRepository.createBloodSample(testSample);

        bloodSampleRepository.updateProgressStatus(testSample.id, BloodSample.ProgressStatus.TESTED);

        BloodSample updatedSample = bloodSampleRepository.findBloodSampleById(testSample.id);
        assertNotNull(updatedSample);
        assertEquals(BloodSample.ProgressStatus.TESTED, updatedSample.progressStatus);
    }

    @Test
    void deleteBloodSample_ShouldRemoveSampleFromRepository() {
        bloodSampleRepository.createBloodSample(testSample);

        bloodSampleRepository.deleteBloodSample(testSample.id);

        BloodSample retrievedSample = bloodSampleRepository.findBloodSampleById(testSample.id);
        assertNull(retrievedSample);
    }

    @Test
    void clear_ShouldRemoveAllSamplesFromRepository() {
        bloodSampleRepository.createBloodSample(testSample);

        bloodSampleRepository.clear();

        List<BloodSample> samples = bloodSampleRepository.listAllBloodSamples();
        assertTrue(samples.isEmpty());
    }
}

