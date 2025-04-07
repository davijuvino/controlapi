package br.com.controlapi.services;

import br.com.controlapi.constants.Msg;
import br.com.controlapi.dto.UserDto;
import br.com.controlapi.exception.NotFoundException;
import br.com.controlapi.exception.UserCreationException;
import br.com.controlapi.exception.UserUpdateException;
import br.com.controlapi.model.User;
import br.com.controlapi.repository.UserRepository;
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

        assertThrows(NotFoundException.class, () -> userServices.getUserById(1L));
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

        assertThrows(NotFoundException.class, () -> userServices.updateUser(1L, userDto));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_LogsError_WhenExceptionOccurs() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(UserUpdateException.class, () -> userServices.updateUser(userId, userDto));

    }

    @Test
    void updateUser_WithAllFields_SuccessfullyUpdatesUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("New Name");
        userDto.setEmail("new@example.com");
        userDto.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");
        existingUser.setCreateAt(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userServices.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newPassword", result.getPassword());
        assertNotNull(result.getUpdateAt());
        assertTrue(result.getUpdateAt().isAfter(existingUser.getCreateAt()));
    }

    @Test
    void updateUser_ReturnsAccurateUserDto() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("Updated Name");
        userDto.setEmail("updated@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Original Name");
        existingUser.setEmail("original@example.com");
        existingUser.setCreateAt(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userServices.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertNotNull(result.getUpdateAt());
        assertTrue(result.getUpdateAt().isAfter(existingUser.getCreateAt()));
        assertEquals(existingUser.getCreateAt(), result.getCreateAt());

    }

    @Test
    void updateUser_ThrowsException_WhenInvalidDataProvided() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setEmail("invalid-email");  // Invalid email format

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("existing@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Invalid data"));

        assertThrows(UserUpdateException.class, () -> userServices.updateUser(userId, userDto));

    }

    @Test
    void updateUser_ThrowsResourceNotFoundException_WithCorrectUserId() {
        Long userId = 999L;
        UserDto userDto = new UserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userServices.updateUser(userId, userDto));

        String expectedMessage = String.format(Msg.USER_NOT_FOUND, userId);
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
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

        assertThrows(NotFoundException.class, () -> userServices.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}
