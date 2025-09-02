package test.java.lsit.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lsit.Models.Donor;
import lsit.Repositories.DonorRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DonorRepositoryTest {

    private DonorRepository donorRepository;
    private Donor testDonor;

    @BeforeEach
    void setUp() {
        donorRepository = new DonorRepository();
        testDonor = new Donor();
        testDonor.id = UUID.randomUUID();
        testDonor.name = "John Doe";
        testDonor.birthdate = "1990-01-01";
        testDonor.email = "john.doe@example.com";
        testDonor.bloodType = "O+";
        testDonor.eligibilityForm = true;
        testDonor.healthCheck = true;

        donorRepository.clear(); // Clear the repository before each test
    }

    @Test
    void createDonor_ShouldAddDonorToRepository() {
        donorRepository.createDonor(testDonor);

        Donor retrievedDonor = donorRepository.findDonorById(testDonor.id);
        assertNotNull(retrievedDonor);
        assertEquals(testDonor.id, retrievedDonor.id);
        assertEquals(testDonor.name, retrievedDonor.name);
    }

    @Test
    void findDonorById_ShouldReturnCorrectDonor() {
        donorRepository.createDonor(testDonor);

        Donor retrievedDonor = donorRepository.findDonorById(testDonor.id);
        assertNotNull(retrievedDonor);
        assertEquals(testDonor.id, retrievedDonor.id);
        assertEquals(testDonor.email, retrievedDonor.email);
    }

    @Test
    void findDonorById_WhenNotFound_ShouldReturnNull() {
        Donor retrievedDonor = donorRepository.findDonorById(UUID.randomUUID());
        assertNull(retrievedDonor);
    }

    @Test
    void listAllDonors_ShouldReturnAllDonors() {
        donorRepository.createDonor(testDonor);

        Donor secondDonor = new Donor();
        secondDonor.id = UUID.randomUUID();
        secondDonor.name = "Jane Doe";
        secondDonor.birthdate = "1985-05-10";
        secondDonor.email = "jane.doe@example.com";
        secondDonor.bloodType = "A+";
        secondDonor.eligibilityForm = true;
        secondDonor.healthCheck = true;

        donorRepository.createDonor(secondDonor);

        List<Donor> donors = donorRepository.listAllDonors();
        assertNotNull(donors);
        assertEquals(2, donors.size());
        assertTrue(donors.contains(testDonor));
        assertTrue(donors.contains(secondDonor));
    }

    @Test
    void updateDonorEligibilityFormStatus_ShouldUpdateEligibility() {
        donorRepository.createDonor(testDonor);

        donorRepository.updateDonorEligibilityFormStatus(testDonor.id, false);

        Donor updatedDonor = donorRepository.findDonorById(testDonor.id);
        assertNotNull(updatedDonor);
        assertFalse(updatedDonor.eligibilityForm);
    }

    @Test
    void updateDonorHealthCheckStatus_ShouldUpdateHealthCheck() {
        donorRepository.createDonor(testDonor);

        donorRepository.updateDonorHealthCheckStatus(testDonor.id, false);

        Donor updatedDonor = donorRepository.findDonorById(testDonor.id);
        assertNotNull(updatedDonor);
        assertFalse(updatedDonor.healthCheck);
    }

    @Test
    void updateDonorDetails_ShouldUpdateDonorInformation() {
        donorRepository.createDonor(testDonor);

        Donor updatedDonor = new Donor();
        updatedDonor.name = "John Smith";
        updatedDonor.birthdate = "1992-02-02";
        updatedDonor.email = "john.smith@example.com";
        updatedDonor.bloodType = "AB+";

        donorRepository.updateDonorDetails(testDonor.id, updatedDonor);

        Donor retrievedDonor = donorRepository.findDonorById(testDonor.id);
        assertNotNull(retrievedDonor);
        assertEquals(updatedDonor.name, retrievedDonor.name);
        assertEquals(updatedDonor.birthdate, retrievedDonor.birthdate);
        assertEquals(updatedDonor.email, retrievedDonor.email);
        assertEquals(updatedDonor.bloodType, retrievedDonor.bloodType);
    }

    @Test
    void deleteDonor_ShouldRemoveDonorFromRepository() {
        donorRepository.createDonor(testDonor);

        donorRepository.deleteDonor(testDonor.id);

        Donor retrievedDonor = donorRepository.findDonorById(testDonor.id);
        assertNull(retrievedDonor);
    }

    @Test
    void clear_ShouldRemoveAllDonorsFromRepository() {
        donorRepository.createDonor(testDonor);

        donorRepository.clear();

        List<Donor> donors = donorRepository.listAllDonors();
        assertTrue(donors.isEmpty());
    }
}
