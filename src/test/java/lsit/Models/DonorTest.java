package test.java.lsit.Models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DonorTest {

    @Test
    void testDonorInitialization() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        LocalDate birthdate = LocalDate.of(1985, 5, 15);
        String email = "johndoe@example.com";
        String bloodType = "O+";
        boolean eligibilityForm = true;
        boolean healthCheck = true;
        List<BloodSample> bloodSamples = new ArrayList<>();
        List<EligibilityForm> eligibilityForms = new ArrayList<>();
        List<HealthCheck> healthChecks = new ArrayList<>();

        // Act
        Donor donor = new Donor();
        donor.id = id;
        donor.name = name;
        donor.birthdate = birthdate;
        donor.email = email;
        donor.bloodType = bloodType;
        donor.eligibilityForm = eligibilityForm;
        donor.healthCheck = healthCheck;
        donor.bloodSamples = bloodSamples;
        donor.eligibilityForms = eligibilityForms;
        donor.healthChecks = healthChecks;

        // Assert
        assertEquals(id, donor.id);
        assertEquals(name, donor.name);
        assertEquals(birthdate, donor.birthdate);
        assertEquals(email, donor.email);
        assertEquals(bloodType, donor.bloodType);
        assertTrue(donor.eligibilityForm);
        assertTrue(donor.healthCheck);
        assertEquals(bloodSamples, donor.bloodSamples);
        assertEquals(eligibilityForms, donor.eligibilityForms);
        assertEquals(healthChecks, donor.healthChecks);
    }

    @Test
    void testDefaultValues() {
        // Act
        Donor donor = new Donor();

        // Assert
        assertNull(donor.id);
        assertNull(donor.name);
        assertNull(donor.birthdate);
        assertNull(donor.email);
        assertNull(donor.bloodType);
        assertFalse(donor.eligibilityForm);
        assertFalse(donor.healthCheck);
        assertNull(donor.bloodSamples);
        assertNull(donor.eligibilityForms);
        assertNull(donor.healthChecks);
    }

    @Test
    void testAddBloodSample() {
        // Arrange
        Donor donor = new Donor();
        donor.bloodSamples = new ArrayList<>();
        BloodSample bloodSample = new BloodSample();
        bloodSample.id = UUID.randomUUID();

        // Act
        donor.bloodSamples.add(bloodSample);

        // Assert
        assertEquals(1, donor.bloodSamples.size());
        assertEquals(bloodSample, donor.bloodSamples.get(0));
    }

    @Test
    void testAddEligibilityForm() {
        // Arrange
        Donor donor = new Donor();
        donor.eligibilityForms = new ArrayList<>();
        EligibilityForm form = new EligibilityForm();

        // Act
        donor.eligibilityForms.add(form);

        // Assert
        assertEquals(1, donor.eligibilityForms.size());
        assertEquals(form, donor.eligibilityForms.get(0));
    }

    @Test
    void testAddHealthCheck() {
        // Arrange
        Donor donor = new Donor();
        donor.healthChecks = new ArrayList<>();
        HealthCheck healthCheck = new HealthCheck();

        // Act
        donor.healthChecks.add(healthCheck);

        // Assert
        assertEquals(1, donor.healthChecks.size());
        assertEquals(healthCheck, donor.healthChecks.get(0));
    }
}
