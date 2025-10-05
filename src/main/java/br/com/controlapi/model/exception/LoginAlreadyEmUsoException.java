package br.com.controlapi.model.exception;

public class LoginAlreadyEmUsoException extends EntidadeEmUsoException{
    public LoginAlreadyEmUsoException() {
        super("Login já está em uso!" );
    }
}
