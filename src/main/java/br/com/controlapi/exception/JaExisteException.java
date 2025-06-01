package br.com.controlapi.exception;

public class JaExisteException extends RuntimeException {
    public JaExisteException(String chave) {
        super(String.format("O chave '%s' já está cadastrado.", chave));
    }
}
