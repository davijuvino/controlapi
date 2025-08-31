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
        when(usuarioServices.listar()).thenReturn(users);

        ResponseEntity<List<UsuariosDto>> response = usuarioController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(usuarioServices, times(1)).listar();
    }

    @Test
    void getUserById_ReturnsUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.buscarPorId(1L)).thenReturn(user);

        ResponseEntity<UsuariosDto> response = usuarioController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(usuarioServices, times(1)).buscarPorId(1L);
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.inserir(user)).thenReturn(user);

        ResponseEntity<UsuariosDto> response = usuarioController.criar(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(usuarioServices, times(1)).inserir(user);
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        UsuariosDto user = new UsuariosDto();
        when(usuarioServices.alterar(1L, user)).thenReturn(user);

        ResponseEntity<UsuariosDto> response = usuarioController.atualizar(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(usuarioServices, times(1)).alterar(1L, user);
    }
}