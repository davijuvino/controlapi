package br.com.controlapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcessoDto {

	private String token;

	//TODO implementar retornar o usuario e liberacoes (authorities)
	
	public AcessoDto(String token) {
		super();
		this.token = token;
	}

	
	
	
	
	
}
