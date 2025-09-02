import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EligibilityFormController.class)
public class EligibilityFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EligibilityFormRepository eligibilityFormRepository;

    @MockBean
    private DonorRepository donorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private EligibilityForm testForm;
    private Donor testDonor;

    @BeforeEach
    void setUp() {
        testDonor = new Donor();
        testDonor.setId(UUID.randomUUID());
        testDonor.setName("Test Donor");

        testForm = new EligibilityForm();
        testForm.setId(UUID.randomUUID());
        testForm.setDonor(testDonor);
        testForm.setSubmissionDate(LocalDate.now());
        testForm.setWeight(70.0);
        testForm.setRecentIllness(false);
        testForm.setMedications(false);
        testForm.setLastDonationDate(LocalDate.now().minusMonths(3));
        testForm.setEligible(true);
    }

    @Test
    @WithMockUser
    void createEligibilityForm_ShouldReturnCreatedForm() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(eligibilityFormRepository.save(any(EligibilityForm.class))).thenReturn(testForm);

        mockMvc.perform(post("/eligibility-forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testForm.getId().toString()))
                .andExpect(jsonPath("$.eligible").value(true));

        verify(eligibilityFormRepository).save(any(EligibilityForm.class));
    }

    @Test
    @WithMockUser
    void getEligibilityForm_ShouldReturnForm() throws Exception {
        when(eligibilityFormRepository.findById(testForm.getId())).thenReturn(Optional.of(testForm));

        mockMvc.perform(get("/eligibility-forms/" + testForm.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testForm.getId().toString()))
                .andExpect(jsonPath("$.weight").value(70.0))
                .andExpect(jsonPath("$.eligible").value(true));
    }

    @Test
    @WithMockUser
    void getEligibilityForm_WhenNotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(eligibilityFormRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/eligibility-forms/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAllEligibilityForms_ShouldReturnList() throws Exception {
        EligibilityForm secondForm = new EligibilityForm();
        secondForm.setId(UUID.randomUUID());
        secondForm.setDonor(testDonor);
        secondForm.setEligible(false);

        when(eligibilityFormRepository.findAll()).thenReturn(Arrays.asList(testForm, secondForm));

        mockMvc.perform(get("/eligibility-forms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testForm.getId().toString()))
                .andExpect(jsonPath("$[1].eligible").value(false));
    }

    @Test
    @WithMockUser
    void getFormsByDonor_ShouldReturnDonorForms() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(eligibilityFormRepository.findByDonor(testDonor)).thenReturn(Arrays.asList(testForm));

        mockMvc.perform(get("/eligibility-forms/donor/" + testDonor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testForm.getId().toString()));
    }

    @Test
    @WithMockUser
    void updateEligibilityForm_ShouldReturnUpdatedForm() throws Exception {
        when(eligibilityFormRepository.findById(testForm.getId())).thenReturn(Optional.of(testForm));
        when(eligibilityFormRepository.save(any(EligibilityForm.class))).thenReturn(testForm);

        testForm.setWeight(75.0);

        mockMvc.perform(put("/eligibility-forms/" + testForm.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(75.0));
    }

    @Test
    @WithMockUser
    void deleteEligibilityForm_ShouldReturn204() throws Exception {
        when(eligibilityFormRepository.findById(testForm.getId())).thenReturn(Optional.of(testForm));
        doNothing().when(eligibilityFormRepository).deleteById(testForm.getId());

        mockMvc.perform(delete("/eligibility-forms/" + testForm.getId()))
                .andExpect(status().isNoContent());

        verify(eligibilityFormRepository).deleteById(testForm.getId());
    }

    @Test
    @WithMockUser
    void checkEligibility_ShouldReturnEligibilityStatus() throws Exception {
        when(eligibilityFormRepository.findById(testForm.getId())).thenReturn(Optional.of(testForm));

        mockMvc.perform(get("/eligibility-forms/" + testForm.getId() + "/check-eligibility")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligible").value(true));
    }

    @Test
    @WithMockUser
    void createEligibilityForm_WithInvalidData_ShouldReturn400() throws Exception {
        testForm.setWeight(-1.0); // Invalid weight

        mockMvc.perform(post("/eligibility-forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void accessWithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/eligibility-forms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
