package br.com.controlapi.model;

import br.com.controlapi.dto.PerfilUsuarioDto;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name = "npl_perfil_usuario")
@Getter
@Setter
@NoArgsConstructor
public class PerfilUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuarios usuarios;
	
	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;

	public PerfilUsuario(PerfilUsuarioDto perfilUsuarioDto) {
		BeanUtils.copyProperties(perfilUsuarioDto, this);
		this.usuarios = Optional.ofNullable(perfilUsuarioDto.getUsuario())
				.map(Usuarios::new)
				.orElse(null);
		this.perfil = Optional.ofNullable(perfilUsuarioDto.getPerfil())
				.map(Perfil::new)
				.orElse(null);
	}
	
}
