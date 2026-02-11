package it.arico.manage_users.model;

import java.util.Set;

import it.arico.manage_users.enums.RolesType;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String name;
    private String surname;
    private String taxCode;
    private Long id;
    private Set<RolesType> roles;
}
