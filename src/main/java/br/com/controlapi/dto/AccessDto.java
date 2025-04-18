package br.com.controlapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessDto {

	private String token;

	//TODO implementar retornar o usuario e liberacoes (authorities)
	
	public AccessDto(String token) {
		super();
		this.token = token;
	}

	
	
	
	
	
}
