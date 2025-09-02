package lsit.Controllers;

import lsit.Models.BloodSample;
import lsit.Repositories.BloodSampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BloodSampleControllerTest {

    @Mock
    private BloodSampleRepository bloodSampleRepository;

    @InjectMocks
    private BloodSampleController bloodSampleController;

    private BloodSample sampleBloodSample;
    private UUID sampleId;
    private UUID donorId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        donorId = UUID.randomUUID();
        sampleBloodSample = new BloodSample();
        sampleBloodSample.id = sampleId;
        sampleBloodSample.donorId = donorId;
        sampleBloodSample.bloodType = "A+";
        sampleBloodSample.qualityStatus = BloodSample.QualityStatus.PENDING;
        sampleBloodSample.progressStatus = BloodSample.ProgressStatus.COLLECTED;
    }

    @Test
    void createBloodSample_Success() {
        when(bloodSampleRepository.createBloodSample(any(BloodSample.class))).thenReturn(sampleBloodSample);

        ResponseEntity<BloodSample> response = bloodSampleController.createBloodSample(sampleBloodSample);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBloodSample, response.getBody());
        verify(bloodSampleRepository).createBloodSample(sampleBloodSample);
    }

    @Test
    void getBloodSample_Success() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(sampleBloodSample);

        ResponseEntity<BloodSample> response = bloodSampleController.getBloodSample(sampleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBloodSample, response.getBody());
    }

    @Test
    void getBloodSample_NotFound() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(null);

        ResponseEntity<BloodSample> response = bloodSampleController.getBloodSample(sampleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void listBloodSamplesByDonor_Success() {
        List<BloodSample> samples = Arrays.asList(sampleBloodSample);
        when(bloodSampleRepository.listBloodSamplesByDonor(donorId)).thenReturn(samples);

        ResponseEntity<List<BloodSample>> response = bloodSampleController.listBloodSamplesByDonor(donorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samples, response.getBody());
    }

    @Test
    void listBloodSamplesByDonor_NoContent() {
        when(bloodSampleRepository.listBloodSamplesByDonor(donorId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<BloodSample>> response = bloodSampleController.listBloodSamplesByDonor(donorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void listAllBloodSamples_Success() {
        List<BloodSample> samples = Arrays.asList(sampleBloodSample);
        when(bloodSampleRepository.listAllBloodSamples()).thenReturn(samples);

        ResponseEntity<?> response = bloodSampleController.listAllBloodSamples();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(samples, response.getBody());
    }

    @Test
    void updateQualityStatus_Success() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(sampleBloodSample);
        
        ResponseEntity<BloodSample> response = bloodSampleController.updateQualityStatus(
            sampleId, BloodSample.QualityStatus.PASSED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BloodSample.QualityStatus.PASSED, response.getBody().qualityStatus);
    }

    @Test
    void updateProgressStatus_Success() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(sampleBloodSample);
        
        ResponseEntity<BloodSample> response = bloodSampleController.updateProgressStatus(
            sampleId, BloodSample.ProgressStatus.PROCESSED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BloodSample.ProgressStatus.PROCESSED, response.getBody().progressStatus);
    }

    @Test
    void deleteBloodSample_Success() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(sampleBloodSample);

        ResponseEntity<Void> response = bloodSampleController.deleteBloodSample(sampleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bloodSampleRepository).deleteBloodSample(sampleId);
    }

    @Test
    void deleteBloodSample_NotFound() {
        when(bloodSampleRepository.findBloodSampleById(sampleId)).thenReturn(null);

        ResponseEntity<Void> response = bloodSampleController.deleteBloodSample(sampleId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bloodSampleRepository, never()).deleteBloodSample(sampleId);
    }
}
