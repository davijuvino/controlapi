package br.com.controlapi.exception;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}
