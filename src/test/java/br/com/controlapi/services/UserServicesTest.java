package br.com.controlapi.services;

import br.com.controlapi.dto.UserDto;
import br.com.controlapi.exception.ResourceNotFoundException;
import br.com.controlapi.exception.UserCreationException;
import br.com.controlapi.model.User;
import br.com.controlapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private UserServices userServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the mocked logger into UserServices
        userServices = new UserServices(userRepository) {
            @Override
            protected Logger getLogger() {
                return logger;
            }
        };
    }

    @Test
    void getLogger_ReturnsCorrectLogger() {
        UserServices userServices = new UserServices(null);
        Logger expectedLogger = LoggerFactory.getLogger(UserServices.class);
        Logger actualLogger = userServices.getLogger();

        assertEquals(expectedLogger, actualLogger);
    }

    @Test
    void createUser_SuccessfullyCreatesUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        User user = new User(userDto);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userServices.createUser(userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ThrowsException_WhenEmailExists() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(UserCreationException.class, () -> userServices.createUser(userDto));
        verify(userRepository, times(1)).existsByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userServices.getAllUsers();

        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ReturnsUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userServices.getUserById(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServices.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser_SuccessfullyUpdatesUser() {
        UserDto userDto = new UserDto();
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userServices.updateUser(1L, userDto);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ThrowsException_WhenUserNotFound() {
        UserDto userDto = new UserDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServices.updateUser(1L, userDto));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldLogAppropriateMessages() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setEmail("updated@example.com");
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        userServices.updateUser(userId, userDto);

        verify(logger).info("Usuário atualizado com sucesso com ID: {}", userId);

        doThrow(new RuntimeException("Simulated error")).when(userRepository).save(any(User.class));

        assertThrows(UserCreationException.class, () -> userServices.updateUser(userId, userDto));
        verify(logger).error(eq("Erro inesperado ao atualizar usuário: {}"), anyString());
    }

    @Test
    void deleteUser_SuccessfullyDeletesUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userServices.deleteUser(1L);

        assertEquals("Usuário excluído com sucesso!", result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowsException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userServices.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}
