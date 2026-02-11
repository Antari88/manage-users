package it.arico.manage_users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import it.arico.manage_users.enums.RolesType;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolesType name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}