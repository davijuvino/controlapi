package br.com.controlapi.exception;

public class CriacaoException extends RuntimeException {
    public CriacaoException(String causa) {
        super("Erro ao criar recurso: " + causa);
    }
}
