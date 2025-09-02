package lsit.Models;

import java.util.UUID;
import java.time.LocalDate;

public class HealthCheck {
    public UUID id; // Unique identifier for the health check
    public UUID donorId; // References the donor who underwent the health check
    public LocalDate dateTaken; // The date when the health check was conducted
    public String doctorName; // The name of the doctor who performed the health check
    public float measuredIronLevel; // The donor's measured iron level during the health check
    public boolean checkPassed; // True if the donor passed the health check
}
