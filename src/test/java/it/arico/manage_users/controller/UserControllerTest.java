package it.arico.manage_users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.arico.manage_users.exception.EmailAlreadyExistsException;
import it.arico.manage_users.exception.UserNotFoundException;
import it.arico.manage_users.model.UserDTO;
import it.arico.manage_users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("test");
        userDTO.setEmail("test@mail.com");
        userDTO.setName("Mario");
        userDTO.setSurname("Rossi");
        userDTO.setTaxCode("ABCDEF12345");
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("test"));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getUser_shouldReturn200() throws Exception {
        when(userService.getUser(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void getUser_shouldReturn404_whenNotFound() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 1"));
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createUser_shouldReturn200() throws Exception {
        when(userService.createUser(any())).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void createUser_shouldReturn400_whenInvalid() throws Exception {
        userDTO.setEmail(""); // invalido

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages.email").exists());
    }

    @Test
    void createUser_shouldReturn409_whenEmailExists() throws Exception {
        when(userService.createUser(any())).thenThrow(new EmailAlreadyExistsException(userDTO.getEmail()));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already exists: test@mail.com"));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateUser_shouldReturn200() throws Exception {
        when(userService.updateUser(anyLong(), any())).thenReturn(userDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    void updateUser_shouldReturn404_whenNotFound() throws Exception {
        when(userService.updateUser(anyLong(), any())).thenThrow(new UserNotFoundException(1L));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 1"));
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteUser_shouldReturn204() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_shouldReturn404_whenNotFound() throws Exception {
        doThrow(new UserNotFoundException(1L)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with id: 1"));
    }
}
