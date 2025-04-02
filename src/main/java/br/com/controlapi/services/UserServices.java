package br.com.controlapi.services;

import br.com.controlapi.dto.UserDto;
import br.com.controlapi.exception.ResourceNotFoundException;
import br.com.controlapi.exception.UserCreationException;
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
                throw new UserCreationException("Usuário com email " + userDTO.getEmail() + " já existe.");
            }
            User user = new User(userDTO);
            User savedUser = userRepository.save(user);
            logger.info("Usuário criado com sucesso com ID: {}", savedUser.getId());
            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar usuário: {}", e.getMessage());
            throw new UserCreationException("Erro inesperado ao criar usuário: " + e.getMessage());
        }
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(UserDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com userId " + userId));
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        return userRepository.findById(userId).map(user -> {
            try {
                BeanUtils.copyProperties(userDto, user, "id", "createAt", "deleteAt", "updateAt");
                user.setUpdateAt(LocalDateTime.now());
                logger.info("Usuário atualizado com sucesso com ID: {}", user.getId());
                return new UserDto(userRepository.save(user));
            } catch (Exception e) {
                logger.error("Erro inesperado ao atualizar usuário: {}", e.getMessage());
                throw new UserCreationException("Erro inesperado ao atualizar usuário: " + e.getMessage());
            }

        }).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com userId " + userId));
    }

    public String deleteUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.deleteById(userId);
            return "Usuário excluído com sucesso!";
        }).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com userId" + userId));
    }
}
