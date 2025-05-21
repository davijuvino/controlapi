package br.com.controlapi.dto;

import br.com.controlapi.model.Perfil;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilDto {

	private Long id;
	private String descricao;
	
	public PerfilDto(Perfil perfil) {
		BeanUtils.copyProperties(perfil, this);
	}
}
