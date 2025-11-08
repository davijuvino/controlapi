package br.com.controlapi.model.exception;

public class EmailNotFoundException extends EntityNotFoundException{
    public EmailNotFoundException() {
        super("E-mail não encontrado.");
    }
}
