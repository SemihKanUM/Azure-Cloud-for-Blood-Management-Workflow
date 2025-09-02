package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.Donor;

@Repository
public class DonorRepository {
    private static HashMap<UUID, Donor> donors = new HashMap<>();

    // Create
    public void createDonor(Donor donor) {
        donor.id = UUID.randomUUID();
        donors.put(donor.id, donor);
    }

    // Read
    public Donor findDonorById(UUID donorId) {
        return donors.get(donorId);
    }

    public List<Donor> listAllDonors() {
        return new ArrayList<>(donors.values());
    }

    // Update
    public void updateDonorEligibilityFormStatus(UUID donorId, boolean eligible) {
        Donor donor = donors.get(donorId);
        if (donor != null) {
            donor.eligibilityForm = eligible;
        }
    }

    public void updateDonorHealthCheckStatus(UUID donorId, boolean healthy) {
        Donor donor = donors.get(donorId);
        if (donor != null) {
            donor.healthCheck = healthy;
        }
    }

    public void updateDonorDetails(UUID donorId, Donor updatedDonor) {
        Donor donor = donors.get(donorId);
        if (donor != null) {
            donor.name = updatedDonor.name;
            donor.birthdate = updatedDonor.birthdate;
            donor.email = updatedDonor.email;
            donor.bloodType = updatedDonor.bloodType;
        }
    }

    // Delete
    public void deleteDonor(UUID donorId) {
        donors.remove(donorId);
    }

    public void clear() {
        donors.clear();
    }
}
