package lsit.Models;

import java.util.UUID;
import java.time.LocalDate;

public class EligibilityForm {
    public UUID id; // Unique identifier for the eligibility form
    public UUID donorId; // References the donor who submitted the form
    public LocalDate dateOfSubmission; // The date when the form was submitted
    public boolean eligible; // True if the donor is eligible
}
