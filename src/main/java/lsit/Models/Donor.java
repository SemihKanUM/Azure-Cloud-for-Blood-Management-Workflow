package lsit.Models;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

public class Donor {
    public UUID id; // Unique identifier for the donor
    public String name; // Donor's full name
    public LocalDate birthdate; // Donor's birthdate
    public String email; // Donor's email address
    public String bloodType; // Donor's blood type
    public boolean eligibilityForm; // True if the donor passed the eligibility form
    public boolean healthCheck; // True if the donor passed the health check
    public List<BloodSample> bloodSamples; // List of blood samples donated by the donor
    public List<EligibilityForm> eligibilityForms; // List of eligibility forms submitted by the donor
    public List<HealthCheck> healthChecks; // List of health checks associated with the donor
}
