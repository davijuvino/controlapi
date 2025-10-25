package br.com.controlapi.model.exception;

import java.io.Serial;

public class InvalidDataException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable causa) {
        super(message, causa);
    }
}
