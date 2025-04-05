package br.com.controlapi.controller;

import br.com.controlapi.dto.UserDto;
import br.com.controlapi.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserServices userServices;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<UserDto> users = Arrays.asList(new UserDto(), new UserDto());
        when(userServices.getAllUsers()).thenReturn(users);

        List<UserDto> result = userController.getAllUsers();

        assertEquals(users, result);
        verify(userServices, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ReturnsUser() {
        UserDto user = new UserDto();
        when(userServices.getUserById(1L)).thenReturn(user);

        UserDto result = userController.getUserById(1L);

        assertEquals(user, result);
        verify(userServices, times(1)).getUserById(1L);
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        UserDto user = new UserDto();
        when(userServices.createUser(user)).thenReturn(user);

        ResponseEntity<UserDto> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userServices, times(1)).createUser(user);
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        UserDto user = new UserDto();
        when(userServices.updateUser(1L, user)).thenReturn(user);

        UserDto result = userController.updateUser(1L, user);

        assertEquals(user, result);
        verify(userServices, times(1)).updateUser(1L, user);
    }

    @Test
    void deleteUser_ReturnsOkStatus() {
        when(userServices.deleteUser(1L)).thenReturn("User deleted");

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted", response.getBody());
        verify(userServices, times(1)).deleteUser(1L);
    }
}