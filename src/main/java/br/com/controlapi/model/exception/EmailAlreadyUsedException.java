package br.com.controlapi.model.exception;

public class EmailAlreadyUsedException extends EntityInUseException {
    public EmailAlreadyUsedException() {
        super("Email já está em uso.");
    }
}
