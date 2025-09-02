import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWelcomeMessage_ShouldReturnWelcomeMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Blood Center"));
    }

    @Test
    void getUserInfo_WithAuthorizedUser_ShouldReturnUserInfo() throws Exception {
        // Create user attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        attributes.put("email", "test@example.com");
        attributes.put("sub", "12345");
        attributes.put("https://gitlab.org/claims/groups/owner", 
            Arrays.asList("lsit-ken3239/roles/blooddonations"));

        // Create OAuth2User
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        OAuth2User oauth2User = new DefaultOAuth2User(
            authorities, 
            attributes,
            "name"
        );

        // Create OAuth2AuthenticationToken
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
            oauth2User,
            authorities,
            "gitlab"
        );

        mockMvc.perform(get("/user")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name: Test User")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("email: test@example.com")));
    }

    @Test
    void getUserInfo_WithUnauthorizedUser_ShouldReturnUnauthorizedMessage() throws Exception {
        // Create user attributes without required group
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        attributes.put("email", "test@example.com");
        attributes.put("sub", "12345");
        attributes.put("https://gitlab.org/claims/groups/owner", 
            Arrays.asList("some-other-group"));

        // Create OAuth2User
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        OAuth2User oauth2User = new DefaultOAuth2User(
            authorities, 
            attributes,
            "name"
        );

        // Create OAuth2AuthenticationToken
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
            oauth2User,
            authorities,
            "gitlab"
        );

        mockMvc.perform(get("/user")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(
                    "This user does not have the rights to view page contents")));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    void getRoles_ShouldReturnUserRoles() throws Exception {
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().string("Roles: ROLE_USER, ROLE_ADMIN"));
    }

    @Test
    void getRoles_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/roles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void getRoles_WithBasicUser_ShouldReturnBasicRole() throws Exception {
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(content().string("Roles: ROLE_USER"));
    }

    @Test
    void getUserInfo_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserInfo_WithMalformedToken_ShouldReturnBadRequest() throws Exception {
        // Create user attributes with missing required fields
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "Test User");
        // Missing other required attributes

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        OAuth2User oauth2User = new DefaultOAuth2User(
            authorities, 
            attributes,
            "name"
        );

        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
            oauth2User,
            authorities,
            "gitlab"
        );

        mockMvc.perform(get("/user")
                .principal(authentication))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHealthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk());
    }
}
