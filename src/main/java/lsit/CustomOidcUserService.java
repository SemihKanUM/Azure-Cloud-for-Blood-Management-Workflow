package lsit;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // default implementation to fetch the user
        OidcUser oidcUser = super.loadUser(userRequest);

        // Extract groups from the token 
        List<String> groups = oidcUser.getAttribute("https://gitlab.org/claims/groups/owner");
        System.out.println(groups);
        // Map groups to Spring Security authorities
        Collection<GrantedAuthority> customAuthorities = groups.stream()
            .map(group -> {
                // Split the group string by "/" and take the last part
                String role = group.substring(group.lastIndexOf("/") + 1).toUpperCase();
                return new SimpleGrantedAuthority("ROLE_" + role);
            })            
            .collect(Collectors.toList());
        System.out.println("customAuthorities");
        System.out.println(customAuthorities);
        // Add the authorities to the OidcUser
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) oidcUser.getAuthorities();
        System.out.println("authorities");
        System.out.println(authorities);
        //authorities.addAll(customAuthorities);

        //return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        // Return the user with the custom authorities
        return new DefaultOidcUser(customAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
