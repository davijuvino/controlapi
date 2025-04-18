package br.com.controlapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDto {
	private String username;
	private String password;
}
