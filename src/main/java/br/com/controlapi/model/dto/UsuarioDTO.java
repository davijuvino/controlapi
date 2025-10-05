package br.com.controlapi.model.dto;

import br.com.controlapi.config.Constantes;
import br.com.controlapi.model.entity.Autorizacoes;
import br.com.controlapi.model.entity.Usuario;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

	private Long id;
	@NotBlank
	@Pattern(regexp = Constantes.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;
	@Size(max = 50)
	private String primeiroNome;
	@Size(max = 50)
	private String ultimoNome;
	@Email
	@Size(min = 5, max = 254)
	private String email;
	private boolean ativado = false;
	@Size(min = 2, max = 6)
	private String langChave;
	private String criadoPor;
	private Instant criadoData;
	private String ultimaModificacaoPor;
	private Instant ultimaModificacaoData;
	private Set<String> autorizacoes;
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.login = usuario.getLogin();
		this.primeiroNome = usuario.getPrimeiroNome();
		this.ultimoNome = usuario.getUltimoNome();
		this.email = usuario.getEmail();
		this.ativado = usuario.isAtivado();
		this.langChave = usuario.getLangChave();
		this.criadoPor = usuario.getCriadoPor();
		this.criadoData = usuario.getCriadoData();
		this.ultimaModificacaoPor = usuario.getUltimaModificacaoPor();
		this.ultimaModificacaoData = usuario.getUltimaModificacaoData();
		this.autorizacoes = usuario.getAuthorities().stream()
				.map(Autorizacoes::getNome)
				.collect(Collectors.toSet());
	}
}
