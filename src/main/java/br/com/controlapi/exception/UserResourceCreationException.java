package br.com.controlapi.exception;

public class UserResourceCreationException extends RuntimeException{
    public UserResourceCreationException(String message) {
        super(message);
    }
}
