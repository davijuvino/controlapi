package br.com.controlapi.service;

import java.time.Instant;
import java.util.UUID;

import br.com.controlapi.dto.PaginacaoDTO;
import br.com.controlapi.dto.UsuarioDTO;
import br.com.controlapi.entity.Usuario;
import br.com.controlapi.entity.UsuarioVerificador;
import br.com.controlapi.entity.enums.TipoSituacaoUsuario;
import br.com.controlapi.repository.UsuarioRepository;
import br.com.controlapi.repository.UsuarioVerificadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public void inserir(UsuarioDTO dto) {
		Usuario usuario = criarUsuarioEntity(dto);
		usuarioRepository.save(usuario);
	}

	@Transactional
	public void inserirNovoUsuario(UsuarioDTO dto) {
		Usuario usuario = criarUsuarioEntity(dto);
		usuario.setSituacao(TipoSituacaoUsuario.PENDENTE);
		usuarioRepository.save(usuario);

		UsuarioVerificador verificador = criarUsuarioVerificador(usuario);
		usuarioVerificadorRepository.save(verificador);

		emailService.enviarEmailTexto(
				dto.getEmail(),
				"Novo usuário cadastrado",
				"Você está recebendo um email de cadastro. O número para validação é: " + verificador.getUuid()
		);
	}

	@Transactional
	public String verificarCadastro(String uuid) {
		return usuarioVerificadorRepository.findByUuid(uuid)
				.map(verificador -> {
					if (verificador.getDataExpiracao().isBefore(Instant.now())) {
						usuarioVerificadorRepository.delete(verificador);
						return "Tempo de verificação expirado";
					}

					Usuario usuario = verificador.getUsuario();
					usuario.setSituacao(TipoSituacaoUsuario.ATIVO);
					usuarioRepository.save(usuario);

					return "Usuário Verificado";
				}).orElse("Usuário não verificado");
	}

	@Transactional
	public UsuarioDTO alterar(UsuarioDTO dto, Long id) {
		usuarioRepository.findById(id).ifPresentOrElse(
				usuarioAtualizar -> {
					usuarioAtualizar.setNome(dto.getNome());
					usuarioAtualizar.setEmail(dto.getEmail());
					if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
						usuarioAtualizar.setSenha(passwordEncoder.encode(dto.getSenha()));
					}
					usuarioRepository.save(usuarioAtualizar);
				}, () -> {
					throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
				}
		);
		dto.setId(id);
        return dto;
    }

	@Transactional
	public void excluir(Long id) {
		usuarioRepository.findById(id).ifPresentOrElse(
				usuarioRepository::delete,	() -> {
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

	private Usuario criarUsuarioEntity(UsuarioDTO dto) {
		Usuario usuario = new Usuario(dto);
		usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
		return usuario;
	}

	private UsuarioVerificador criarUsuarioVerificador(Usuario usuario) {
		UsuarioVerificador verificador = new UsuarioVerificador();
		verificador.setUsuario(usuario);
		verificador.setUuid(UUID.randomUUID().toString());
		verificador.setDataExpiracao(Instant.now().plusMillis(900000));
		return verificador;
	}
}