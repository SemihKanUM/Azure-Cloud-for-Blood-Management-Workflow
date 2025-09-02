package lsit.Models;

import java.util.UUID;
import java.time.LocalDate;

public class BloodSample {
    public UUID id; // Unique identifier for the blood sample
    public UUID donorId; // References the donor who provided the blood sample
    public LocalDate collectionDate; // The date when the blood sample was collected

    public QualityStatus qualityStatus; // Enum indicating the quality status of the blood sample
    public ProgressStatus progressStatus; // Enum indicating the progress status of the blood sample

    public enum QualityStatus {
        UNTESTED, APPROVED, REJECTED
    }

    public enum ProgressStatus {
        COLLECTION_FAILED, COLLECTED, TESTED, STORED, DISCARDED
    }
}
