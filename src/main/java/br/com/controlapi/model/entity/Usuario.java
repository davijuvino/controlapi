package br.com.controlapi.model.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import br.com.controlapi.config.Constantes;
import br.com.controlapi.model.dto.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.BeanUtils;

@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table(name = "NPL_USUARIO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usuario extends AbstractAuditoriaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Pattern(regexp = Constantes.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "senha_hash", length = 60, nullable = false)
	private String senha;

	@Size(max = 50)
	@Column(name = "primeiro_nome", length = 50)
	private String primeiroNome;

	@Size(max = 50)
	@Column(name = "ultimo_nome", length = 50)
	private String ultimoNome;

	@Email
	@Size(min = 5, max = 254)
	@Column(length = 254, unique = true)
	private String email;

	@NotNull
	@Column(nullable = false)
	private boolean ativado = false;

	@Size(min = 2, max = 6)
	@Column(name = "lang_chave", length = 6)
	private String langChave;

	@Size(max = 20)
	@Column(name = "ativando_chave", length = 20)
	@JsonIgnore
	private String ativandoChave;

	@Size(max = 20)
	@Column(name = "resetar_chave", length = 20)
	@JsonIgnore
	private String resetarChave;

	@Column(name = "resetar_data")
	private Instant resetarData = null;


	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "npl_usuario_autorizacoes",
			joinColumns = {@JoinColumn(name = "usuario_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "autorizacoes_nome", referencedColumnName = "nome")})
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Autorizacoes> authorities = new HashSet<>();
	
	public Usuario(UsuarioDTO usuario) {
		BeanUtils.copyProperties(usuario, this);
	}
}
