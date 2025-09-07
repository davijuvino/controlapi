package br.com.controlapi.dto;

import br.com.controlapi.entity.Perfil;
import org.springframework.beans.BeanUtils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilDTO {

	private Long id;
	private String descricao;
	
	public PerfilDTO(Perfil perfil) {
		BeanUtils.copyProperties(perfil, this);
	}
}
