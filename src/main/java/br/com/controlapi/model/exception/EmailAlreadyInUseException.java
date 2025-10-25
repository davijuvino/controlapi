package br.com.controlapi.model.exception;

public class EmailAlreadyInUseException extends EntityInUseException {
    public EmailAlreadyInUseException() {
        super("Email já está em uso.");
    }
}
