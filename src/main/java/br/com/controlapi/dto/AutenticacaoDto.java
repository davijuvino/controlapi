package br.com.controlapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutenticacaoDto {
	private String usuarioNome;
	private String senha;
}
