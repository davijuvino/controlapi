package br.com.controlapi.model.exception;

public class LoginAlreadyInUseException extends EntityInUseException {
    public LoginAlreadyInUseException() {
        super("Login já está em uso!" );
    }
}
