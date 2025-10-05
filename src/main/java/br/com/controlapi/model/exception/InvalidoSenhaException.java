package br.com.controlapi.model.exception;

public class InvalidoSenhaException extends DadosInvalidosException{
    public InvalidoSenhaException() {
        super("Senha inválida");
    }
}
