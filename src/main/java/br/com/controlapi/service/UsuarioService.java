package br.com.controlapi.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import br.com.controlapi.dto.PaginacaoDTO;
import br.com.controlapi.dto.UsuarioDTO;
import br.com.controlapi.entity.UsuarioEntity;
import br.com.controlapi.entity.UsuarioVerificadorEntity;
import br.com.controlapi.entity.enums.TipoSituacaoUsuario;
import br.com.controlapi.repository.UsuarioRepository;
import br.com.controlapi.repository.UsuarioVerificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioVerificadorRepository usuarioVerificadorRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository,
						  UsuarioVerificadorRepository usuarioVerificadorRepository,
						  PasswordEncoder passwordEncoder,
						  EmailService emailService) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioVerificadorRepository = usuarioVerificadorRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Transactional(readOnly = true)
	public PaginacaoDTO<UsuarioDTO> listarTodos(Pageable pageable) {
		Page<UsuarioDTO> page = usuarioRepository.findAll(pageable).map(UsuarioDTO::new);
		return new PaginacaoDTO<>(page);
	}

	@Transactional
	public void inserir(UsuarioDTO usuario) {
		UsuarioEntity usuarioEntity = criarUsuarioEntity(usuario);
		usuarioRepository.save(usuarioEntity);
	}

	@Transactional
	public void inserirNovoUsuario(UsuarioDTO usuario) {
		UsuarioEntity usuarioEntity = criarUsuarioEntity(usuario);
		usuarioEntity.setSituacao(TipoSituacaoUsuario.PENDENTE);
		usuarioRepository.save(usuarioEntity);

		UsuarioVerificadorEntity verificador = criarUsuarioVerificador(usuarioEntity);
		usuarioVerificadorRepository.save(verificador);

		emailService.enviarEmailTexto(
				usuario.getEmail(),
				"Novo usuário cadastrado",
				"Você está recebendo um email de cadastro. O número para validação é: " + verificador.getUuid()
		);
	}

	@Transactional
	public String verificarCadastro(String uuid) {
		return usuarioVerificadorRepository.findByUuid(UUID.fromString(uuid))
				.map(verificador -> {
					if (verificador.getDataExpiracao().isBefore(Instant.now())) {
						usuarioVerificadorRepository.delete(verificador);
						return "Tempo de verificação expirado";
					}

					UsuarioEntity usuario = verificador.getUsuario();
					usuario.setSituacao(TipoSituacaoUsuario.ATIVO);
					usuarioRepository.save(usuario);

					return "Usuário Verificado";
				}).orElse("Usuário não verificado");
	}

	@Transactional
	public UsuarioDTO alterar(UsuarioDTO usuario) {
		UsuarioEntity usuarioEntity = criarUsuarioEntity(usuario);
		return new UsuarioDTO(usuarioRepository.save(usuarioEntity));
	}

	@Transactional
	public void excluir(Long id) {
		usuarioRepository.findById(id)
				.ifPresentOrElse(usuarioRepository::delete,	() -> {
							throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
						}
				);
	}

	@Transactional(readOnly = true)
	public UsuarioDTO buscarPorId(Long id) {
		return usuarioRepository.findById(id)
				.map(UsuarioDTO::new)
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
	}

	private UsuarioEntity criarUsuarioEntity(UsuarioDTO usuario) {
		UsuarioEntity usuarioEntity = new UsuarioEntity(usuario);
		usuarioEntity.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return usuarioEntity;
	}

	private UsuarioVerificadorEntity criarUsuarioVerificador(UsuarioEntity usuarioEntity) {
		UsuarioVerificadorEntity verificador = new UsuarioVerificadorEntity();
		verificador.setUsuario(usuarioEntity);
		verificador.setUuid(UUID.randomUUID());
		verificador.setDataExpiracao(Instant.now().plusMillis(900000));
		return verificador;
	}
}