package br.com.controlapi.services;

import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.exception.*;
import br.com.controlapi.model.*;
import br.com.controlapi.repository.*;
import jakarta.validation.Valid;
import br.com.controlapi.model.enums.SituacaoUsuario;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServices {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServices.class);
    private static final long TEMPO_EXPIRACAO_VERIFICACAO_MS = 900000; // 15 minutos

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final UsuarioVerificadorRepository usuarioVerificadorRepository;

    @Transactional
    public UsuariosDto criar(@Valid UsuariosDto usuariosDto) {
        validarEmailExistente(usuariosDto.getEmail());

        try {
            Usuarios usuario = new Usuarios(usuariosDto);
            Usuarios usuarioSalvo = usuarioRepository.save(usuario);
            logger.info("Usuário criado com sucesso: {}", usuarioSalvo.getEmail());
            return new UsuariosDto(usuarioSalvo);
        } catch (Exception e) {
            logger.error("Erro ao criar usuário: {}", e.getMessage());
            throw new CriacaoException("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @Transactional
    public void criarNovoUsuario(UsuariosDto usuarioDto) {
        validarEmailExistente(usuarioDto.getEmail());

        Usuarios usuario = criarUsuarioPendente(usuarioDto);
        UsuarioVerificador verificador = criarTokenVerificacao(usuario);

        enviarEmailVerificacao(usuarioDto.getEmail(), verificador.getUuid());
    }

    @Transactional
    public String verificarCadastro(String uuid) {
        UUID uuidVerificacao = UUID.fromString(uuid);
        UsuarioVerificador verificador = usuarioVerificadorRepository.findByUuid(uuidVerificacao)
                .orElseThrow(() -> new VerificacaoException("Código de verificação inválido"));

        validarExpiracaoToken(verificador);

        ativarUsuario(verificador.getUsuarios());
        usuarioVerificadorRepository.delete(verificador);

        return "Usuário verificado com sucesso";
    }

    public List<UsuariosDto> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuariosDto::new)
                .collect(Collectors.toList());
    }

    public UsuariosDto buscarPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(UsuariosDto::new)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado com ID: {}", usuarioId);
                    return new NaoEncontradoException("Usuário não encontrado com ID " + usuarioId);
                });
    }

    @Transactional
    public UsuariosDto atualizar(Long usuarioId, UsuariosDto usuariosDto) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> atualizarUsuario(usuario, usuariosDto))
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado para atualização: {}", usuarioId);
                    return new NaoEncontradoException("Usuário não encontrado com ID " + usuarioId);
                });
    }

    @Transactional
    public void deletar(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .ifPresentOrElse(usuario -> {
                    usuarioRepository.deleteById(usuarioId);
                    logger.info("Usuário deletado com sucesso: {}", usuarioId);
                }, () -> {
                    logger.warn("Tentativa de deletar usuário não encontrado: {}", usuarioId);
                    throw new NaoEncontradoException("Usuário não encontrado com ID " + usuarioId);
                });
    }

    // Métodos privados auxiliares

    private Usuarios criarUsuarioPendente(UsuariosDto usuarioDto) {
        Usuarios usuario = new Usuarios(usuarioDto);
        usuario.setSenha(usuarioDto.getSenha());
        usuario.setSituacao(SituacaoUsuario.PENDENTE);
        return usuarioRepository.save(usuario);
    }

    private UsuarioVerificador criarTokenVerificacao(Usuarios usuario) {
        UsuarioVerificador verificador = new UsuarioVerificador();
        verificador.setUsuarios(usuario);
        verificador.setUuid(UUID.randomUUID());
        verificador.setDataExpiracao(Instant.now().plusMillis(TEMPO_EXPIRACAO_VERIFICACAO_MS));
        return usuarioVerificadorRepository.save(verificador);
    }

    private void enviarEmailVerificacao(String email, UUID uuid) {
        emailService.enviarEmailTexto(email,
                "Confirmação de cadastro",
                "Por favor, verifique seu cadastro usando o seguinte código: " + uuid);
    }

    private void validarExpiracaoToken(UsuarioVerificador verificador) {
        if (verificador.getDataExpiracao().isBefore(Instant.now())) {
            usuarioVerificadorRepository.delete(verificador);
            throw new VerificacaoException("Tempo de verificação expirado");
        }
    }

    private void ativarUsuario(Usuarios usuario) {
        usuario.setSituacao(SituacaoUsuario.ATIVO);
        usuarioRepository.save(usuario);
    }

    private void validarEmailExistente(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            logger.warn("Tentativa de criar usuário com email já existente: {}", email);
            throw new CriacaoException("Email " + email + " já está em uso");
        }
    }

    private UsuariosDto atualizarUsuario(Usuarios usuario, UsuariosDto usuariosDto) {
        try {
            BeanUtils.copyProperties(usuariosDto, usuario,
                    "id", "criadoAt", "deletadoAt", "atualizadoAt");
            usuario.setAtualizadoAt(LocalDateTime.now());

            Usuarios usuarioAtualizado = usuarioRepository.save(usuario);
            logger.info("Usuário atualizado com sucesso: {}", usuario.getId());
            return new UsuariosDto(usuarioAtualizado);
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário {}: {}", usuario.getId(), e.getMessage());
            throw new AtualizarException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}