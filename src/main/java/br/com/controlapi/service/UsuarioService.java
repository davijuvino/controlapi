package br.com.controlapi.service;


import br.com.controlapi.model.dto.UsuarioDTO;
import br.com.controlapi.model.entity.Autorizacoes;
import br.com.controlapi.model.entity.Usuario;
import br.com.controlapi.model.exception.EmailAlreadyEmUsoException;
import br.com.controlapi.model.exception.LoginAlreadyEmUsoException;
import br.com.controlapi.repository.AutorizacoesRepository;
import br.com.controlapi.repository.UsuarioRepository;
import br.com.controlapi.security.AutorizacoesConstantes;
import br.com.controlapi.service.util.RandomUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

	private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

	private final UsuarioRepository usuarioRepository;
	private final AutorizacoesRepository autorizacoesRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final CacheManager cacheManager;

	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository, AutorizacoesRepository autorizacoesRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService, CacheManager cacheManager) {
		this.usuarioRepository = usuarioRepository;
        this.autorizacoesRepository = autorizacoesRepository;
        this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
        this.cacheManager = cacheManager;
    }

    public Usuario registrarUsuario(UsuarioDTO usuarioDTO, String senha) {
		usuarioRepository.findOneByLogin(usuarioDTO.getLogin().toLowerCase()).ifPresent(existeUsuario -> {
			if (!existeUsuario.isAtivado()) {
				usuarioRepository.delete(existeUsuario);
				this.limpaUsuarioCaches(existeUsuario);
			} else {
				throw new LoginAlreadyEmUsoException();
			}
		});
		usuarioRepository.findOneByEmailIgnoreCase(usuarioDTO.getEmail()).ifPresent(existeUsuario -> {
			if (!existeUsuario.isAtivado()) {
				usuarioRepository.delete(existeUsuario);
				usuarioRepository.flush();
				this.limpaUsuarioCaches(existeUsuario);
			} else {
				throw new EmailAlreadyEmUsoException();
			}
		});
		Usuario novoUsuario = new Usuario();
		String senhaCriptografada = passwordEncoder.encode(senha);
		novoUsuario.setLogin(usuarioDTO.getLogin().toLowerCase());
		// ao inicialiar o novo usuario, gerar a senha criptografada
		novoUsuario.setSenha(senhaCriptografada);
		novoUsuario.setPrimeiroNome(usuarioDTO.getPrimeiroNome());
		novoUsuario.setUltimoNome(usuarioDTO.getUltimoNome());
		novoUsuario.setEmail(usuarioDTO.getEmail().toLowerCase());
		novoUsuario.setLangChave(usuarioDTO.getLangChave());
		// novo usuario nao esta ativado
		novoUsuario.setAtivado(false);
		// novo usuario registrar a chave
		novoUsuario.setAtivandoChave(RandomUtil.generateActivationKey());
		Set<Autorizacoes> autorizacoes = new HashSet<>();
		autorizacoesRepository.findById(AutorizacoesConstantes.USUARIO).ifPresent(autorizacoes::add);
		novoUsuario.setAuthorities(autorizacoes);
		usuarioRepository.save(novoUsuario);
		this.limpaUsuarioCaches(novoUsuario);
		log.debug("Criar informações para usuario: {}", novoUsuario);
		return novoUsuario;
    }

	private void limpaUsuarioCaches(Usuario usuario) {
		Objects.requireNonNull(cacheManager.getCache(UsuarioRepository.USUARIOS_BY_LOGIN_CACHE)).evict(usuario.getLogin());
		Objects.requireNonNull(cacheManager.getCache(UsuarioRepository.USUARIOS_BY_EMAIL_CACHE)).evict(usuario.getEmail());
	}
}