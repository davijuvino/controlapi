package br.com.controlapi.services;

import br.com.controlapi.constants.Mensagem;
import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.exception.NaoEncontradoException;
import br.com.controlapi.exception.CriacaoException;
import br.com.controlapi.exception.AtualizarException;
import br.com.controlapi.model.Usuarios;
import br.com.controlapi.repository.UsuarioRepository;
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
public class UsuarioServices {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServices.class);
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuariosDto createUser(@Valid UsuariosDto usuariosDto) {
        logger.info("Iniciando a criação do usuário com email: {}", usuariosDto.getEmail());
        try {
            if (usuarioRepository.existsByEmail(usuariosDto.getEmail())) {
                throw new CriacaoException(String.format(Mensagem.INFO_JA_EXISTE, usuariosDto.getEmail()));
            }
            Usuarios usuarios = new Usuarios(usuariosDto);
            Usuarios savedUsuarios = usuarioRepository.save(usuarios);
            logger.info(Mensagem.INFO_SUCESSO_CRIAR, savedUsuarios.getId());
            return new UsuariosDto(savedUsuarios);
        } catch (Exception e) {
            logger.error(Mensagem.INFO_ERRO_CRIAR, e.getMessage());
            throw new CriacaoException(String.format(Mensagem.INFO_ERRO_CRIAR, e.getMessage()));
        }
    }

    public List<UsuariosDto> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(UsuariosDto::new)
                .collect(Collectors.toList());
    }

    public UsuariosDto getUserById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).map(UsuariosDto::new)
                .orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, usuarioId)));
    }

    public UsuariosDto updateUser(Long usuarioId, UsuariosDto usuariosDto) {
        return usuarioRepository.findById(usuarioId).map(user -> {
            try {
                BeanUtils.copyProperties(usuariosDto, user, "id", "criadoAt", "deletadoAt", "atualizadoAt");
                user.setAtualizadoAt(LocalDateTime.now());
                Usuarios updatedUsuarios = usuarioRepository.save(user);
                logger.info(Mensagem.INFO_SUCESSO_ATUALIZAR, usuarioId);
                return new UsuariosDto(updatedUsuarios);
            } catch (Exception e) {
                logger.error(Mensagem.INFO_ERRO_ATUALIZAR, e.getMessage());
                throw new AtualizarException(String.format(Mensagem.INFO_ERRO_ATUALIZAR, e.getMessage()));
            }

        }).orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, usuarioId)));
    }

    public String deleteUser(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).map(user -> {
            usuarioRepository.deleteById(usuarioId);
            return Mensagem.INFO_SUCESSO_DELETE;
        }).orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, usuarioId)));
    }
}
