package br.com.controlapi.security;

import org.springframework.security.core.AuthenticationException;
import java.io.Serial;

public class UserNotActivatedException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String msg) {
        super(msg);
    }

    public UserNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }
}
