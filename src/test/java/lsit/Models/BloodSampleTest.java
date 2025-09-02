package test.java.lsit.Models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BloodSampleTest {

    @Test
    void testBloodSampleInitialization() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID donorId = UUID.randomUUID();
        LocalDate collectionDate = LocalDate.now();
        BloodSample.QualityStatus qualityStatus = BloodSample.QualityStatus.UNTESTED;
        BloodSample.ProgressStatus progressStatus = BloodSample.ProgressStatus.COLLECTED;

        // Act
        BloodSample bloodSample = new BloodSample();
        bloodSample.id = id;
        bloodSample.donorId = donorId;
        bloodSample.collectionDate = collectionDate;
        bloodSample.qualityStatus = qualityStatus;
        bloodSample.progressStatus = progressStatus;

        // Assert
        assertEquals(id, bloodSample.id);
        assertEquals(donorId, bloodSample.donorId);
        assertEquals(collectionDate, bloodSample.collectionDate);
        assertEquals(qualityStatus, bloodSample.qualityStatus);
        assertEquals(progressStatus, bloodSample.progressStatus);
    }

    @Test
    void testQualityStatusEnumValues() {
        // Act
        BloodSample.QualityStatus[] statuses = BloodSample.QualityStatus.values();

        // Assert
        assertArrayEquals(new BloodSample.QualityStatus[]{
                BloodSample.QualityStatus.UNTESTED,
                BloodSample.QualityStatus.APPROVED,
                BloodSample.QualityStatus.REJECTED
        }, statuses);
    }

    @Test
    void testProgressStatusEnumValues() {
        // Act
        BloodSample.ProgressStatus[] statuses = BloodSample.ProgressStatus.values();

        // Assert
        assertArrayEquals(new BloodSample.ProgressStatus[]{
                BloodSample.ProgressStatus.COLLECTION_FAILED,
                BloodSample.ProgressStatus.COLLECTED,
                BloodSample.ProgressStatus.TESTED,
                BloodSample.ProgressStatus.STORED,
                BloodSample.ProgressStatus.DISCARDED
        }, statuses);
    }

    @Test
    void testDefaultValues() {
        // Act
        BloodSample bloodSample = new BloodSample();

        // Assert
        assertNull(bloodSample.id);
        assertNull(bloodSample.donorId);
        assertNull(bloodSample.collectionDate);
        assertNull(bloodSample.qualityStatus);
        assertNull(bloodSample.progressStatus);
    }
}
