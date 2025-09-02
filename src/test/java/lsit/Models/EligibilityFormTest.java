package test.java.lsit.Models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EligibilityFormTest {

    @Test
    void testEligibilityFormInitialization() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID donorId = UUID.randomUUID();
        LocalDate dateOfSubmission = LocalDate.of(2024, 11, 29);
        boolean eligible = true;

        // Act
        EligibilityForm form = new EligibilityForm();
        form.id = id;
        form.donorId = donorId;
        form.dateOfSubmission = dateOfSubmission;
        form.eligible = eligible;

        // Assert
        assertEquals(id, form.id);
        assertEquals(donorId, form.donorId);
        assertEquals(dateOfSubmission, form.dateOfSubmission);
        assertTrue(form.eligible);
    }

    @Test
    void testDefaultValues() {
        // Act
        EligibilityForm form = new EligibilityForm();

        // Assert
        assertNull(form.id);
        assertNull(form.donorId);
        assertNull(form.dateOfSubmission);
        assertFalse(form.eligible);
    }
}

