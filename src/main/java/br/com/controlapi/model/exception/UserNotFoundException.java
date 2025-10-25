package br.com.controlapi.model.exception;

import java.io.Serial;

public class UserNotFoundException extends EntityNotFoundException {

	@Serial
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String mensagem) {
		super(mensagem);
	}
	
	public UserNotFoundException(Long usuarioId) {
		this(String.format("Não existe um cadastro de usuário com código %d", usuarioId));
	}
	
}
