package lsit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOidcUserService customOidcUserService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOidcUserService) // Using custom service to determine user roles
                )
            )
            .authorizeHttpRequests(authorize -> authorize
                // General permissions
                .requestMatchers("/").permitAll() // Home page accessible by everyone

                // Restrict access to donors/listall
                .requestMatchers(HttpMethod.GET, "/donors/listall").hasRole("ADMIN") // None of the designated roles have access due to information protection

                // Donor access
                .requestMatchers(HttpMethod.GET, "/donors/{id}").hasRole("DONOR") // View donor details
                .requestMatchers(HttpMethod.GET, "/donors/{id}/health-status").hasRole("DONOR") // Check health status
                .requestMatchers(HttpMethod.GET, "/donors/{id}/eligibility-status").hasRole("DONOR") // Check eligibility status
                .requestMatchers(HttpMethod.POST, "/eligibility-forms/create").hasRole("DONOR") // Submit eligibility form
                .requestMatchers(HttpMethod.GET, "/eligibility-forms/{id}").hasRole("DONOR") // View specific eligibility form
                .requestMatchers(HttpMethod.GET, "/eligibility-forms/by-donor/{donorId}").hasRole("DONOR") // View all eligibility forms by the donor

                // Medical Staff access - Specific Donor Information
                .requestMatchers(HttpMethod.GET, "/donors/{id}").hasRole("DONOR") // View donor details
                .requestMatchers(HttpMethod.GET, "/donors/{id}/health-status").hasRole("MEDICAL-STAFF") // Check health status
                .requestMatchers(HttpMethod.GET, "/donors/{id}/eligibility-status").hasRole("MEDICAL-STAFF") // Check eligibility status

                // Medical Staff access - Eligibility Forms
                .requestMatchers(HttpMethod.GET, "/eligibility-forms/listall").hasRole("MEDICAL-STAFF") // View all eligibility forms
                .requestMatchers(HttpMethod.GET, "/eligibility-forms/{id}").hasRole("MEDICAL-STAFF") // View specific eligibility form
                .requestMatchers(HttpMethod.GET, "/eligibility-forms/by-donor/{donorId}").hasRole("MEDICAL-STAFF") // View all eligibility forms by donor
                .requestMatchers(HttpMethod.PUT, "/eligibility-forms/{id}").hasRole("MEDICAL-STAFF") // Update eligibility form
                .requestMatchers(HttpMethod.DELETE, "/eligibility-forms/{id}").hasRole("MEDICAL-STAFF") // Delete eligibility form

                // Medical Staff access - Health Checks
                .requestMatchers(HttpMethod.POST, "/health-checks/create").hasRole("MEDICAL-STAFF") // Record new health checks
                .requestMatchers(HttpMethod.GET, "/health-checks/listall").hasRole("MEDICAL-STAFF") // View all health checks
                .requestMatchers(HttpMethod.GET, "/health-checks/{id}").hasRole("MEDICAL-STAFF") // View specific health check
                .requestMatchers(HttpMethod.GET, "/health-checks/by-donor/{donorId}").hasRole("MEDICAL-STAFF") // View all health checks by donor
                .requestMatchers(HttpMethod.PUT, "/health-checks/{id}").hasRole("MEDICAL-STAFF") // Update health check
                .requestMatchers(HttpMethod.DELETE, "/health-checks/{id}").hasRole("MEDICAL-STAFF") // Delete health check

                // Blood Management Unit access
                .requestMatchers(HttpMethod.POST, "/blood-samples/create").hasRole("BLOOD-MANAGEMENT-UNIT") // Record new blood samples
                .requestMatchers(HttpMethod.GET, "/blood-samples/listall").hasRole("BLOOD-MANAGEMENT-UNIT") // View all blood samples
                .requestMatchers(HttpMethod.GET, "/blood-samples/{id}").hasRole("BLOOD-MANAGEMENT-UNIT") // Retrieve specific blood sample
                .requestMatchers(HttpMethod.GET, "/blood-samples/by-donor/{donorId}").hasRole("BLOOD-MANAGEMENT-UNIT") // View all blood samples by donor
                .requestMatchers(HttpMethod.PATCH, "/blood-samples/{id}/quality").hasRole("BLOOD-MANAGEMENT-UNIT") // Update quality status
                .requestMatchers(HttpMethod.PATCH, "/blood-samples/{id}/progress").hasRole("BLOOD-MANAGEMENT-UNIT") // Update progress status
                .requestMatchers(HttpMethod.DELETE, "/blood-samples/{id}").hasRole("BLOOD-MANAGEMENT-UNIT") // Delete blood sample

                // Fallback for authenticated users
                .anyRequest().authenticated() // All other pages require login
            );

        return http.build();
    }


}