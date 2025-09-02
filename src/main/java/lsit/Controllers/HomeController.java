package lsit.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity get(){
        return ResponseEntity.ok("Welcome to the Blood Center");
    }

    @GetMapping("/user")
    public String getUser(OAuth2AuthenticationToken authentication) throws Exception{
        var groups = (List<String>)authentication.getPrincipal().getAttribute("https://gitlab.org/claims/groups/owner");
        var userAttributes = authentication.getPrincipal().getAttributes();

        String userInfo = "<pre> \n" +
            userAttributes.entrySet().parallelStream().collect(
                StringBuilder::new,
                (s, e) -> s.append(e.getKey()).append(": ").append(e.getValue()),
                (a, b) -> a.append("\n").append(b)
            ) +
            "</pre>";

        if(!groups.contains("lsit-ken3239/roles/blooddonations")){
            return "This user does not have the rights to view page contents" + "\n" + userInfo;
        };

        return userInfo;
    }

    @GetMapping("/roles")
    public String printRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get all authorities (roles)
        String roles = authentication.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(", "));

        System.out.println("Current user roles: " + roles);

        return "Roles: " + roles;
    }

}



