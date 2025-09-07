package br.com.controlapi.dto;

import br.com.controlapi.entity.PermissaoPerfilRecurso;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissaoPerfilRecursoDTO {
	
	private Long id;
	private PerfilDTO perfil;	
	private RecursoDTO recurso;
	
	public PermissaoPerfilRecursoDTO(PermissaoPerfilRecurso permissaoPerfilRecurso) {
		BeanUtils.copyProperties(permissaoPerfilRecurso, this);
		if(permissaoPerfilRecurso != null && permissaoPerfilRecurso.getRecurso() != null) {
			this.recurso = new RecursoDTO(permissaoPerfilRecurso.getRecurso());
		}
		if(permissaoPerfilRecurso != null && permissaoPerfilRecurso.getPerfil() != null) {
			this.perfil = new PerfilDTO(permissaoPerfilRecurso.getPerfil());
		}		
	}
}
