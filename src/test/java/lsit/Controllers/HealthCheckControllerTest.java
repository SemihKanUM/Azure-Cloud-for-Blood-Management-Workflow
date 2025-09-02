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

@WebMvcTest(HealthCheckController.class)
public class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckRepository healthCheckRepository;

    @MockBean
    private DonorRepository donorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private HealthCheck testHealthCheck;
    private Donor testDonor;

    @BeforeEach
    void setUp() {
        testDonor = new Donor();
        testDonor.setId(UUID.randomUUID());
        testDonor.setName("Test Donor");

        testHealthCheck = new HealthCheck();
        testHealthCheck.setId(UUID.randomUUID());
        testHealthCheck.setDonor(testDonor);
        testHealthCheck.setCheckDate(LocalDate.now());
        testHealthCheck.setHemoglobinLevel(14.0);
        testHealthCheck.setBloodPressure("120/80");
        testHealthCheck.setPulseRate(72);
        testHealthCheck.setTemperature(98.6);
        testHealthCheck.setWeight(70.0);
        testHealthCheck.setPassed(true);
        testHealthCheck.setNotes("Healthy donor");
    }

    @Test
    @WithMockUser
    void createHealthCheck_ShouldReturnCreatedHealthCheck() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(healthCheckRepository.save(any(HealthCheck.class))).thenReturn(testHealthCheck);

        mockMvc.perform(post("/health-checks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testHealthCheck.getId().toString()))
                .andExpect(jsonPath("$.hemoglobinLevel").value(14.0))
                .andExpect(jsonPath("$.passed").value(true));

        verify(healthCheckRepository).save(any(HealthCheck.class));
    }

    @Test
    @WithMockUser
    void getHealthCheck_ShouldReturnHealthCheck() throws Exception {
        when(healthCheckRepository.findById(testHealthCheck.getId())).thenReturn(Optional.of(testHealthCheck));

        mockMvc.perform(get("/health-checks/" + testHealthCheck.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testHealthCheck.getId().toString()))
                .andExpect(jsonPath("$.hemoglobinLevel").value(14.0))
                .andExpect(jsonPath("$.bloodPressure").value("120/80"));
    }

    @Test
    @WithMockUser
    void getHealthCheck_WhenNotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(healthCheckRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/health-checks/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAllHealthChecks_ShouldReturnList() throws Exception {
        HealthCheck secondHealthCheck = new HealthCheck();
        secondHealthCheck.setId(UUID.randomUUID());
        secondHealthCheck.setDonor(testDonor);
        secondHealthCheck.setPassed(false);

        when(healthCheckRepository.findAll()).thenReturn(Arrays.asList(testHealthCheck, secondHealthCheck));

        mockMvc.perform(get("/health-checks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testHealthCheck.getId().toString()))
                .andExpect(jsonPath("$[1].passed").value(false));
    }

    @Test
    @WithMockUser
    void getHealthChecksByDonor_ShouldReturnDonorHealthChecks() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(healthCheckRepository.findByDonor(testDonor)).thenReturn(Arrays.asList(testHealthCheck));

        mockMvc.perform(get("/health-checks/donor/" + testDonor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testHealthCheck.getId().toString()));
    }

    @Test
    @WithMockUser
    void updateHealthCheck_ShouldReturnUpdatedHealthCheck() throws Exception {
        when(healthCheckRepository.findById(testHealthCheck.getId())).thenReturn(Optional.of(testHealthCheck));
        when(healthCheckRepository.save(any(HealthCheck.class))).thenReturn(testHealthCheck);

        testHealthCheck.setHemoglobinLevel(15.0);

        mockMvc.perform(put("/health-checks/" + testHealthCheck.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hemoglobinLevel").value(15.0));
    }

    @Test
    @WithMockUser
    void deleteHealthCheck_ShouldReturn204() throws Exception {
        when(healthCheckRepository.findById(testHealthCheck.getId())).thenReturn(Optional.of(testHealthCheck));
        doNothing().when(healthCheckRepository).deleteById(testHealthCheck.getId());

        mockMvc.perform(delete("/health-checks/" + testHealthCheck.getId()))
                .andExpect(status().isNoContent());

        verify(healthCheckRepository).deleteById(testHealthCheck.getId());
    }

    @Test
    @WithMockUser
    void createHealthCheck_WithInvalidData_ShouldReturn400() throws Exception {
        testHealthCheck.setHemoglobinLevel(-1.0); // Invalid hemoglobin level

        mockMvc.perform(post("/health-checks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void accessWithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/health-checks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getLatestHealthCheck_ShouldReturnMostRecentCheck() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(healthCheckRepository.findFirstByDonorOrderByCheckDateDesc(testDonor))
                .thenReturn(Optional.of(testHealthCheck));

        mockMvc.perform(get("/health-checks/donor/" + testDonor.getId() + "/latest")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testHealthCheck.getId().toString()));
    }

    @Test
    @WithMockUser
    void validateHealthMetrics_ShouldReturnValidationResult() throws Exception {
        mockMvc.perform(post("/health-checks/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").exists());
    }
    
    @Test
    @WithMockUser
    void validateHealthMetrics_WithInvalidMetrics_ShouldReturnFailure() throws Exception {
        testHealthCheck.setHemoglobinLevel(8.0); // Assuming this is too low
        testHealthCheck.setPulseRate(150); // Assuming this is too high
    
        mockMvc.perform(post("/health-checks/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").exists());
    }
    
    @Test
    @WithMockUser
    void getHealthChecksByDateRange_ShouldReturnFilteredChecks() throws Exception {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
    
        when(healthCheckRepository.findByCheckDateBetween(startDate, endDate))
                .thenReturn(Arrays.asList(testHealthCheck));
    
        mockMvc.perform(get("/health-checks/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testHealthCheck.getId().toString()));
    }
    
    @Test
    @WithMockUser
    void getHealthCheckStats_ShouldReturnStatistics() throws Exception {
        when(healthCheckRepository.countByPassed(true)).thenReturn(10L);
        when(healthCheckRepository.countByPassed(false)).thenReturn(2L);
        when(healthCheckRepository.averageHemoglobinLevel()).thenReturn(14.2);
    
        mockMvc.perform(get("/health-checks/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPassed").value(10))
                .andExpect(jsonPath("$.totalFailed").value(2))
                .andExpect(jsonPath("$.averageHemoglobin").value(14.2));
    }
    
    @Test
    @WithMockUser
    void updateHealthCheckNotes_ShouldUpdateNotes() throws Exception {
        when(healthCheckRepository.findById(testHealthCheck.getId())).thenReturn(Optional.of(testHealthCheck));
        when(healthCheckRepository.save(any(HealthCheck.class))).thenReturn(testHealthCheck);
    
        String newNotes = "Updated health check notes";
        testHealthCheck.setNotes(newNotes);
    
        mockMvc.perform(patch("/health-checks/" + testHealthCheck.getId() + "/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newNotes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value(newNotes));
    }
    
    @Test
    @WithMockUser
    void bulkUpdateHealthChecks_ShouldUpdateMultipleChecks() throws Exception {
        List<HealthCheck> healthChecks = Arrays.asList(testHealthCheck);
        when(healthCheckRepository.saveAll(anyList())).thenReturn(healthChecks);
    
        mockMvc.perform(put("/health-checks/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(healthChecks)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testHealthCheck.getId().toString()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getFailedHealthChecks_ShouldReturnOnlyFailedChecks() throws Exception {
        HealthCheck failedCheck = new HealthCheck();
        failedCheck.setId(UUID.randomUUID());
        failedCheck.setPassed(false);
    
        when(healthCheckRepository.findByPassed(false)).thenReturn(Arrays.asList(failedCheck));
    
        mockMvc.perform(get("/health-checks/failed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].passed").value(false));
    }
    
    @Test
    @WithMockUser
    void searchHealthChecks_ShouldReturnMatchingChecks() throws Exception {
        when(healthCheckRepository.findByDonorNameContainingIgnoreCase(anyString()))
                .thenReturn(Arrays.asList(testHealthCheck));
    
        mockMvc.perform(get("/health-checks/search")
                .param("query", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testHealthCheck.getId().toString()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void exportHealthChecks_ShouldReturnCsvFile() throws Exception {
        mockMvc.perform(get("/health-checks/export")
                .accept("text/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"))
                .andExpect(header().string("Content-Disposition", 
                        containsString("attachment; filename=\"health-checks-")));
    }
    
    @Test
    @WithMockUser
    void validateHealthCheck_WithMissingRequiredFields_ShouldReturn400() throws Exception {
        testHealthCheck.setHemoglobinLevel(null);
        testHealthCheck.setBloodPressure(null);
    
        mockMvc.perform(post("/health-checks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHealthCheck)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void getHealthCheckAuditLog_ShouldReturnAuditEntries() throws Exception {
        mockMvc.perform(get("/health-checks/" + testHealthCheck.getId() + "/audit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void accessAdminEndpoint_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/health-checks/failed")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    }
    
