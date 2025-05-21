package br.com.controlapi.controller;

import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.services.UsuarioServices;
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

public class UsuariosControllerTest {

    @Mock
    private UsuarioServices usuarioServices;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<UsuariosDto> users = Arrays.asList(new UsuariosDto(), new UsuariosDto());
        when(usuarioServices.getAllUsers()).thenReturn(users);

        List<UsuariosDto> result = usuarioController.getAllUsers();

        assertEquals(users, result);
        verify(usuarioServices, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ReturnsUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.getUserById(1L)).thenReturn(user);

        UsuariosDto result = usuarioController.getUserById(1L);

        assertEquals(user, result);
        verify(usuarioServices, times(1)).getUserById(1L);
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.createUser(user)).thenReturn(user);

        ResponseEntity<UsuariosDto> response = usuarioController.criarUsuario(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(usuarioServices, times(1)).createUser(user);
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.updateUser(1L, user)).thenReturn(user);

        UsuariosDto result = usuarioController.atualizarUsuario(1L, user);

        assertEquals(user, result);
        verify(usuarioServices, times(1)).updateUser(1L, user);
    }

    @Test
    void deleteUser_ReturnsOkStatus() {
        when(usuarioServices.deleteUser(1L)).thenReturn("Usuarios deleted");

        ResponseEntity<String> response = usuarioController.deletarUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuarios deleted", response.getBody());
        verify(usuarioServices, times(1)).deleteUser(1L);
    }
}