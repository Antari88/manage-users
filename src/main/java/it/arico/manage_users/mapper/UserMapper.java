package it.arico.manage_users.mapper;

import it.arico.manage_users.model.UserDTO;
import it.arico.manage_users.utils.JwtUtils;
import it.arico.manage_users.entity.Role;
import it.arico.manage_users.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setTaxCode(user.getTaxCode());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        dto.setId(user.getId());  
        
        JwtUtils.getFilteredInfoUserForRole(dto);
        
        return dto;
    }

    public static User toEntity(UserDTO dto, Set<Role> roles) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setTaxCode(dto.getTaxCode());
        user.setRoles(roles);
        user.setId(dto.getId());
        return user;
    }
}