package it.arico.manage_users.model;

import it.arico.manage_users.enums.RolesType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @NotBlank(message = "Tax code is mandatory")
    @Size(min = 11, max = 16, message = "Tax code must be between 11 and 16 characters")
    private String taxCode;

    private Set<RolesType> roles;
}
