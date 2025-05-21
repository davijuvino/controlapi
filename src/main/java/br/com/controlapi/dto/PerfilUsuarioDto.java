package br.com.controlapi.dto;

import br.com.controlapi.model.PerfilUsuario;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class PerfilUsuarioDto {

	private Long id;
	private UsuariosDto usuario;
	private PerfilDto perfil;

	public PerfilUsuarioDto(PerfilUsuario perfilUsuario) {
		BeanUtils.copyProperties(perfilUsuario, this);
		this.usuario = Optional.ofNullable(perfilUsuario.getUsuarios()).map(UsuariosDto::new)
				.orElse(null);
		this.perfil = Optional.ofNullable(perfilUsuario.getPerfil()).map(PerfilDto::new)
				.orElse(null);
	}
	
}
