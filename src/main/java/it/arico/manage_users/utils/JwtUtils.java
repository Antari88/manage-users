package it.arico.manage_users.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import it.arico.manage_users.model.UserDTO;

public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {
    }

    public static String getUsername() {
        Jwt jwt = getJwt();
        return jwt != null ? jwt.getClaimAsString("preferred_username") : null;
    }

    public static void logCurrentUser() {
        String username = getUsername();
        log.info("Operazione eseguita da utente: {}", username);
    }

    private static Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt) {
            return (Jwt) auth.getPrincipal();
        }
        return null;
    }

    public static UserDTO getFilteredInfoUserForRole(UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userFake = new UserDTO();
        if (auth == null) return userFake;

        Jwt jwt = (Jwt) auth.getPrincipal();
        
        Object realmAccessObj = jwt.getClaim("realm_access");
        if (!(realmAccessObj instanceof Map<?, ?> realmAccess)) {
            return userFake;
        }
    
        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof List<?> rolesList)) {
            return userDTO;
        }
    
        List<String> roles = rolesList.stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .toList();

        if (roles.contains("ADMIN")) {
            // nulla può vedere tutto
        } else if (roles.contains("OPERATOR")) {
            userDTO.setTaxCode(null);
        } else { 
            userDTO.setTaxCode(null);
            userDTO.setRoles(null);
        }
        return userDTO;
    }
}
