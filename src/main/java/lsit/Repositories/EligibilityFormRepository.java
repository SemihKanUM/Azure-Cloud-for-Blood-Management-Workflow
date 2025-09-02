package lsit.Repositories;

import java.util.*;
import org.springframework.stereotype.Repository;
import lsit.Models.EligibilityForm;

@Repository
public class EligibilityFormRepository {
    private static HashMap<UUID, EligibilityForm> eligibilityForms = new HashMap<>();

    // Create
    public void createEligibilityForm(EligibilityForm form) {
        form.id = UUID.randomUUID();
        eligibilityForms.put(form.id, form);
    }

    // Read
    public List<EligibilityForm> findEligibilityFormsByDonorId(UUID donorId) {
        List<EligibilityForm> forms = new ArrayList<>();
        for (EligibilityForm form : eligibilityForms.values()) {
            if (form.donorId.equals(donorId)) {
                forms.add(form);
            }
        }
        return forms;
    }

    public List<EligibilityForm> listAllEligibilityForms() {
        return new ArrayList<>(eligibilityForms.values());
    }

    // Update
    public void updateEligibilityForm(UUID formId, EligibilityForm updatedForm) {
        EligibilityForm form = eligibilityForms.get(formId);
        if (form != null) {
            form.dateOfSubmission = updatedForm.dateOfSubmission;
            form.eligible = updatedForm.eligible;
        }
    }

    // Delete
    public void deleteEligibilityForm(UUID formId) {
        eligibilityForms.remove(formId);
    }

    public void clear() {
        eligibilityForms.clear();
    }

}
