package br.com.controlapi.model.exception;

public class InvalidPasswordException extends InvalidDataException {
    public InvalidPasswordException() {
        super("Senha inválida");
    }
}
