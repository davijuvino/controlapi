package br.com.controlapi.exception;

public class AtualizarException extends RuntimeException {
    public AtualizarException(String cause) {
        super("Erro inesperado ao atualizar - causa: " + cause);
    }
}
