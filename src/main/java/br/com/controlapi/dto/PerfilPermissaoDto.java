package br.com.controlapi.dto;

import br.com.controlapi.model.PerfilPermissao;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class PerfilPermissaoDto {
	
	private Long id;
	private PerfilDto perfil;
	private RecursoDto recurso;
	/*
	public PerfilPermissaoDto(PerfilPermissao resourcePermissionProfile) {
		BeanUtils.copyProperties(resourcePermissionProfile, this);
		if(resourcePermissionProfile.getRecurso() != null) {
			this.userResource = new RecursoDto(resourcePermissionProfile.getRecurso());
		}
		if(resourcePermissionProfile.getPerfil() != null) {
			this.profile = new PerfilDto(resourcePermissionProfile.getPerfil());
		}
	}*/

	public PerfilPermissaoDto(PerfilPermissao perfilPermissao) {
		BeanUtils.copyProperties(perfilPermissao, this);
		this.recurso = Optional.ofNullable(perfilPermissao.getRecurso())
				.map(RecursoDto::new)
				.orElse(null);
		this.perfil = Optional.ofNullable(perfilPermissao.getPerfil())
				.map(PerfilDto::new)
				.orElse(null);
	}
}
