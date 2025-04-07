package br.com.controlapi.services;

import br.com.controlapi.constants.Msg;
import br.com.controlapi.dto.UserDto;
import br.com.controlapi.exception.NotFoundException;
import br.com.controlapi.exception.UserCreationException;
import br.com.controlapi.exception.UserUpdateException;
import br.com.controlapi.model.User;
import br.com.controlapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServices {

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);
    private UserRepository userRepository;

    @Transactional
    public UserDto createUser(@Valid UserDto userDTO) {
        logger.info("Iniciando a criação do usuário com email: {}", userDTO.getEmail());
        try {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new UserCreationException(String.format(Msg.USER_ALREADY_EXISTS, userDTO.getEmail()));
            }
            User user = new User(userDTO);
            User savedUser = userRepository.save(user);
            logger.info(Msg.USER_CREATION_SUCCESS, savedUser.getId());
            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error(Msg.USER_CREATION_ERROR, e.getMessage());
            throw new UserCreationException(String.format(Msg.USER_CREATION_ERROR, e.getMessage()));
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(UserDto::new)
                .orElseThrow(() -> new NotFoundException(String.format(Msg.USER_NOT_FOUND, userId)));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        return userRepository.findById(userId).map(user -> {
            try {
                BeanUtils.copyProperties(userDto, user, "id", "createAt", "deleteAt", "updateAt");
                user.setUpdateAt(LocalDateTime.now());
                User updatedUser = userRepository.save(user);
                logger.info(Msg.USER_UPDATE_SUCCESS, userId);
                return new UserDto(updatedUser);
            } catch (Exception e) {
                logger.error(Msg.USER_UPDATE_ERROR, e.getMessage());
                throw new UserUpdateException(String.format(Msg.USER_UPDATE_ERROR, e.getMessage()));
            }

        }).orElseThrow(() -> new NotFoundException(String.format(Msg.USER_NOT_FOUND, userId)));
    }

    public String deleteUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.deleteById(userId);
            return Msg.USER_DELETION_SUCCESS;
        }).orElseThrow(() -> new NotFoundException(String.format(Msg.USER_NOT_FOUND, userId)));
    }
}
