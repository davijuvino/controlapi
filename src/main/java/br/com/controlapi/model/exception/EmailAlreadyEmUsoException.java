package br.com.controlapi.model.exception;

public class EmailAlreadyEmUsoException extends EntidadeEmUsoException {
    public EmailAlreadyEmUsoException() {
        super("Email já está em uso.");
    }
}
