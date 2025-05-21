package br.com.controlapi.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "npl_usuario_verificador")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UsuarioVerificador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private UUID uuid;
	
	@Column(name = "data_expiracao", nullable = false)
	private Instant dataExpiracao;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
	private Usuarios usuarios;
}
