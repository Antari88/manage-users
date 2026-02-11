package it.arico.manage_users.repository;

import it.arico.manage_users.entity.Role;
import it.arico.manage_users.enums.RolesType;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RolesType name);
}
