package br.com.controlapi.exception;

public class ResourceCreationException extends RuntimeException{
    public ResourceCreationException(String message) {
        super(message);
    }
}
