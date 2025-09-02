import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.UUID;
import java.util.Optional;

@WebMvcTest(DonorController.class)
public class DonorControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DonorRepository donorRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Donor testDonor;
    
    @BeforeEach
    void setUp() {
        testDonor = new Donor();
        testDonor.setId(UUID.randomUUID());
        testDonor.setName("Test Donor");
        testDonor.setBloodType("A+");
    }
    
    @Test
    void createDonor_ShouldReturnCreatedDonor() throws Exception {
        when(donorRepository.save(any(Donor.class))).thenReturn(testDonor);
        
        mockMvc.perform(post("/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDonor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testDonor.getId().toString()))
                .andExpect(jsonPath("$.name").value(testDonor.getName()))
                .andExpect(jsonPath("$.bloodType").value(testDonor.getBloodType()));
        
        verify(donorRepository).save(any(Donor.class));
    }
    
    @Test
    void getDonor_ShouldReturnDonor() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        
        mockMvc.perform(get("/donors/" + testDonor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testDonor.getId().toString()))
                .andExpect(jsonPath("$.name").value(testDonor.getName()))
                .andExpect(jsonPath("$.bloodType").value(testDonor.getBloodType()));
    }
    
    @Test
    void getDonor_WhenNotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(donorRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/donors/" + nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getAllDonors_ShouldReturnList() throws Exception {
        Donor secondDonor = new Donor();
        secondDonor.setId(UUID.randomUUID());
        secondDonor.setName("Second Donor");
        secondDonor.setBloodType("B-");
        
        when(donorRepository.findAll()).thenReturn(Arrays.asList(testDonor, secondDonor));
        
        mockMvc.perform(get("/donors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testDonor.getId().toString()))
                .andExpect(jsonPath("$[1].name").value(secondDonor.getName()));
    }
    
    @Test
    void updateDonor_ShouldReturnUpdatedDonor() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        when(donorRepository.save(any(Donor.class))).thenReturn(testDonor);
        
        testDonor.setName("Updated Name");
        
        mockMvc.perform(put("/donors/" + testDonor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDonor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }
    
    @Test
    void deleteDonor_ShouldReturn204() throws Exception {
        when(donorRepository.findById(testDonor.getId())).thenReturn(Optional.of(testDonor));
        doNothing().when(donorRepository).deleteById(testDonor.getId());
        
        mockMvc.perform(delete("/donors/" + testDonor.getId()))
                .andExpect(status().isNoContent());
        
        verify(donorRepository).deleteById(testDonor.getId());
    }
    
    @Test
    void createDonor_WithInvalidData_ShouldReturn400() throws Exception {
        testDonor.setName("");  // Invalid name
        
        mockMvc.perform(post("/donors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDonor)))
                .andExpect(status().isBadRequest());
    }
}
