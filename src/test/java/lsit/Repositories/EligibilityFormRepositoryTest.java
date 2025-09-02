package test.java.lsit.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lsit.Models.EligibilityForm;
import lsit.Repositories.EligibilityFormRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EligibilityFormRepositoryTest {

    private EligibilityFormRepository eligibilityFormRepository;
    private EligibilityForm testForm;
    private UUID donorId;

    @BeforeEach
    void setUp() {
        eligibilityFormRepository = new EligibilityFormRepository();
        
        // Set up a test donorId for the eligibility form
        donorId = UUID.randomUUID();
        
        // Create and populate the test EligibilityForm
        testForm = new EligibilityForm();
        testForm.id = UUID.randomUUID();
        testForm.donorId = donorId;
        testForm.dateOfSubmission = "2024-11-01";
        testForm.eligible = true;

        eligibilityFormRepository.clear(); // Clear the repository before each test
    }

    @Test
    void createEligibilityForm_ShouldAddFormToRepository() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        EligibilityForm retrievedForm = eligibilityFormRepository.findEligibilityFormsByDonorId(donorId).get(0);
        assertNotNull(retrievedForm);
        assertEquals(testForm.id, retrievedForm.id);
        assertEquals(testForm.donorId, retrievedForm.donorId);
        assertEquals(testForm.dateOfSubmission, retrievedForm.dateOfSubmission);
    }

    @Test
    void findEligibilityFormsByDonorId_ShouldReturnCorrectForms() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        EligibilityForm secondForm = new EligibilityForm();
        secondForm.id = UUID.randomUUID();
        secondForm.donorId = donorId;
        secondForm.dateOfSubmission = "2024-11-02";
        secondForm.eligible = false;
        eligibilityFormRepository.createEligibilityForm(secondForm);

        List<EligibilityForm> forms = eligibilityFormRepository.findEligibilityFormsByDonorId(donorId);
        assertNotNull(forms);
        assertEquals(2, forms.size());
        assertTrue(forms.contains(testForm));
        assertTrue(forms.contains(secondForm));
    }

    @Test
    void findEligibilityFormsByDonorId_WhenNoForms_ShouldReturnEmptyList() {
        List<EligibilityForm> forms = eligibilityFormRepository.findEligibilityFormsByDonorId(UUID.randomUUID());
        assertNotNull(forms);
        assertTrue(forms.isEmpty());
    }

    @Test
    void listAllEligibilityForms_ShouldReturnAllForms() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        EligibilityForm secondForm = new EligibilityForm();
        secondForm.id = UUID.randomUUID();
        secondForm.donorId = UUID.randomUUID();
        secondForm.dateOfSubmission = "2024-11-02";
        secondForm.eligible = true;
        eligibilityFormRepository.createEligibilityForm(secondForm);

        List<EligibilityForm> allForms = eligibilityFormRepository.listAllEligibilityForms();
        assertNotNull(allForms);
        assertEquals(2, allForms.size());
    }

    @Test
    void updateEligibilityForm_ShouldUpdateFormDetails() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        EligibilityForm updatedForm = new EligibilityForm();
        updatedForm.id = testForm.id;
        updatedForm.donorId = testForm.donorId;
        updatedForm.dateOfSubmission = "2024-11-05";
        updatedForm.eligible = false;

        eligibilityFormRepository.updateEligibilityForm(testForm.id, updatedForm);

        EligibilityForm retrievedForm = eligibilityFormRepository.findEligibilityFormsByDonorId(donorId).get(0);
        assertNotNull(retrievedForm);
        assertEquals("2024-11-05", retrievedForm.dateOfSubmission);
        assertFalse(retrievedForm.eligible);
    }

    @Test
    void deleteEligibilityForm_ShouldRemoveFormFromRepository() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        eligibilityFormRepository.deleteEligibilityForm(testForm.id);

        List<EligibilityForm> forms = eligibilityFormRepository.findEligibilityFormsByDonorId(donorId);
        assertTrue(forms.isEmpty());
    }

    @Test
    void clear_ShouldRemoveAllFormsFromRepository() {
        eligibilityFormRepository.createEligibilityForm(testForm);

        eligibilityFormRepository.clear();

        List<EligibilityForm> forms = eligibilityFormRepository.listAllEligibilityForms();
        assertTrue(forms.isEmpty());
    }
}

