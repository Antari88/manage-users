package it.arico.manage_users.service;

import it.arico.manage_users.entity.User;
import it.arico.manage_users.exception.EmailAlreadyExistsException;
import it.arico.manage_users.exception.UserNotFoundException;
import it.arico.manage_users.mapper.UserMapper;
import it.arico.manage_users.model.UserDTO;
import it.arico.manage_users.entity.Role;
import it.arico.manage_users.repository.UserRepository;
import it.arico.manage_users.utils.JwtUtils;
import it.arico.manage_users.repository.RoleRepository;
import it.arico.manage_users.model.UserCreatedEventDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<UserDTO> getAllUsers() {
        JwtUtils.logCurrentUser();
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        JwtUtils.logCurrentUser();
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public UserDTO createUser(UserDTO dto) {
        JwtUtils.logCurrentUser();

        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }
        dto.setId(null);
        Set<Role> roles = dto.getRoles().stream()
        .map(roleName -> roleRepository.findByName(roleName)
              .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
        .collect(Collectors.toSet());

    
        User user = UserMapper.toEntity(dto, roles);

        User savedUser = userRepository.save(user);

        eventPublisher.publishEvent(
            new UserCreatedEventDTO(savedUser.getId(), savedUser.getUsername())
        );


        return UserMapper.toDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        JwtUtils.logCurrentUser();
        return userRepository.findById(id).map(user -> {
            user.setUsername(dto.getUsername());
            user.setName(dto.getName());
            user.setSurname(dto.getSurname());
            user.setTaxCode(dto.getTaxCode());

            // Aggiorno ruoli
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);

            User updated = userRepository.save(user);
            return UserMapper.toDTO(updated);
        }).orElseThrow(() -> new UserNotFoundException(id));
        
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        JwtUtils.logCurrentUser();
    }
}
