package br.com.controlapi.services;

import br.com.controlapi.constants.Mensagem;
import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.exception.NaoEncontradoException;
import br.com.controlapi.exception.CriacaoException;
import br.com.controlapi.exception.AtualizarException;
import br.com.controlapi.model.Usuarios;
import br.com.controlapi.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuariosServicesTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private UsuarioServices usuarioServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createUser_SuccessfullyCreatesUser() {
        UsuariosDto usuariosDto = new UsuariosDto();
        usuariosDto.setEmail("test@example.com");
        Usuarios usuarios = new Usuarios(usuariosDto);
        when(usuarioRepository.existsByEmail(usuariosDto.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuarios.class))).thenReturn(usuarios);

        UsuariosDto result = usuarioServices.criar(usuariosDto);

        assertEquals(usuariosDto.getEmail(), result.getEmail());
        verify(usuarioRepository, times(1)).existsByEmail(usuariosDto.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    void createUser_ThrowsException_WhenEmailExists() {
        UsuariosDto usuariosDto = new UsuariosDto();
        usuariosDto.setEmail("test@example.com");
        when(usuarioRepository.existsByEmail(usuariosDto.getEmail())).thenReturn(true);

        assertThrows(CriacaoException.class, () -> usuarioServices.criar(usuariosDto));
        verify(usuarioRepository, times(1)).existsByEmail(usuariosDto.getEmail());
        verify(usuarioRepository, never()).save(any(Usuarios.class));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<Usuarios> usuarios = Arrays.asList(new Usuarios(), new Usuarios());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuariosDto> result = usuarioServices.listar();

        assertEquals(usuarios.size(), result.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ReturnsUser() {
        Usuarios usuarios = new Usuarios();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarios));

        UsuariosDto result = usuarioServices.buscarPorId(1L);

        assertNotNull(result);
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ThrowsException_WhenUserNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> usuarioServices.buscarPorId(1L));
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser_SuccessfullyUpdatesUser() {
        UsuariosDto usuariosDto = new UsuariosDto();
        Usuarios usuarios = new Usuarios();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarios));
        when(usuarioRepository.save(any(Usuarios.class))).thenReturn(usuarios);

        UsuariosDto result = usuarioServices.atualizar(1L, usuariosDto);

        assertNotNull(result);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    void updateUser_ThrowsException_WhenUserNotFound() {
        UsuariosDto usuariosDto = new UsuariosDto();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> usuarioServices.atualizar(1L, usuariosDto));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any(Usuarios.class));
    }

    @Test
    void updateUser_LogsError_WhenExceptionOccurs() {
        Long userId = 1L;
        UsuariosDto usuariosDto = new UsuariosDto();
        Usuarios existingUsuarios = new Usuarios();
        existingUsuarios.setId(userId);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(existingUsuarios));
        when(usuarioRepository.save(any(Usuarios.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(AtualizarException.class, () -> usuarioServices.atualizar(userId, usuariosDto));

    }

    @Test
    void updateUser_WithAllFields_SuccessfullyUpdatesUser() {
        Long userId = 1L;
        UsuariosDto usuariosDto = new UsuariosDto();
        usuariosDto.setNome("New Name");
        usuariosDto.setEmail("new@example.com");
        usuariosDto.setSenha("newPassword");

        Usuarios existingUsuarios = new Usuarios();
        existingUsuarios.setId(userId);
        existingUsuarios.setNome("Old Name");
        existingUsuarios.setEmail("old@example.com");
        existingUsuarios.setSenha("oldPassword");
        existingUsuarios.setCriadoAt(LocalDateTime.now().minusDays(1));

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(existingUsuarios));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuariosDto result = usuarioServices.atualizar(userId, usuariosDto);

        assertNotNull(result);
        assertEquals("New Name", result.getNome());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newPassword", result.getSenha());
        assertNotNull(result.getAtualizadoAt());
        assertTrue(result.getAtualizadoAt().isAfter(existingUsuarios.getCriadoAt()));
    }

    @Test
    void updateUser_ReturnsAccurateUserDto() {
        Long userId = 1L;
        UsuariosDto usuariosDto = new UsuariosDto();
        usuariosDto.setNome("Updated Name");
        usuariosDto.setEmail("updated@example.com");

        Usuarios existingUsuarios = new Usuarios();
        existingUsuarios.setId(userId);
        existingUsuarios.setNome("Original Name");
        existingUsuarios.setEmail("original@example.com");
        existingUsuarios.setCriadoAt(LocalDateTime.now().minusDays(1));

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(existingUsuarios));
        when(usuarioRepository.save(any(Usuarios.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuariosDto result = usuarioServices.atualizar(userId, usuariosDto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Updated Name", result.getNome());
        assertEquals("updated@example.com", result.getEmail());
        assertNotNull(result.getAtualizadoAt());
        assertTrue(result.getAtualizadoAt().isAfter(existingUsuarios.getCriadoAt()));
        assertEquals(existingUsuarios.getCriadoAt(), result.getCriadoAt());

    }

    @Test
    void updateUser_ThrowsException_WhenInvalidDataProvided() {
        Long userId = 1L;
        UsuariosDto usuariosDto = new UsuariosDto();
        usuariosDto.setEmail("invalid-email");  // Invalid email format

        Usuarios existingUsuarios = new Usuarios();
        existingUsuarios.setId(userId);
        existingUsuarios.setEmail("existing@example.com");

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(existingUsuarios));
        when(usuarioRepository.save(any(Usuarios.class))).thenThrow(new RuntimeException("Invalid data"));

        assertThrows(AtualizarException.class, () -> usuarioServices.atualizar(userId, usuariosDto));

    }

    @Test
    void updateUser_ThrowsResourceNotFoundException_WithCorrectUserId() {
        Long userId = 999L;
        UsuariosDto usuariosDto = new UsuariosDto();
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(NaoEncontradoException.class,
                () -> usuarioServices.atualizar(userId, usuariosDto));

        String expectedMessage = String.format(Mensagem.INFO_NAO_ENCONTRADO, userId);
        assertEquals(expectedMessage, exception.getMessage());
        verify(usuarioRepository).findById(userId);
        verify(usuarioRepository, never()).save(any(Usuarios.class));
    }

    @Test
    void deleteUser_ThrowsException_WhenUserNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> usuarioServices.deletar(1L));
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).deleteById(1L);
    }
}
