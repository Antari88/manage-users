package it.arico.manage_users.service;

import it.arico.manage_users.entity.Role;
import it.arico.manage_users.entity.User;
import it.arico.manage_users.enums.RolesType;
import it.arico.manage_users.exception.EmailAlreadyExistsException;
import it.arico.manage_users.exception.RoleNotFoundException;
import it.arico.manage_users.exception.UserNotFoundException;
import it.arico.manage_users.model.UserCreatedEventDTO;
import it.arico.manage_users.model.UserDTO;
import it.arico.manage_users.repository.RoleRepository;
import it.arico.manage_users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName(RolesType.DEVELOPER);

        user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setEmail("test@mail.com");
        user.setName("Mario");
        user.setSurname("Rossi");
        user.setTaxCode("ABCDEF12345");
        user.setRoles(Set.of(role));

        userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setEmail("test@mail.com");
        userDTO.setName("Mario");
        userDTO.setSurname("Rossi");
        userDTO.setTaxCode("ABCDEF12345");
        userDTO.setRoles(Set.of(RolesType.OWNER));
    }

    // =========================
    // GET USER
    // =========================

    @Test
    void getUser_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUser(1L);

        assertNotNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(1L));
    }

    // =========================
    // CREATE USER
    // =========================

    @Test
    void createUser_shouldSaveUser() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RolesType.OWNER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishEvent(any(UserCreatedEventDTO.class));
    }

    @Test
    void createUser_shouldThrowEmailAlreadyExists() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createUser(userDTO));
    }

    @Test
    void createUser_shouldThrowRoleNotFound() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(roleRepository.findByName(RolesType.OWNER)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class,
                () -> userService.createUser(userDTO));
    }

    // =========================
    // UPDATE USER
    // =========================

    @Test
    void updateUser_shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(RolesType.OWNER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_shouldThrowUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(1L, userDTO));
    }

    // =========================
    // DELETE USER
    // =========================

    @Test
    void deleteUser_shouldDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(1L));
    }
    
    @Test
    void updateUser_shouldUpdateUserSuccessfully() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(RolesType.OWNER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, userDTO);
    
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }


    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());

        verify(userRepository).findAll();
    }

    
}
