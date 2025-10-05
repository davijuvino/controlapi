package br.com.controlapi.security;

import org.springframework.security.core.AuthenticationException;
import java.io.Serial;

public class UserNaoAtivadoException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNaoAtivadoException(String msg) {
        super(msg);
    }

    public UserNaoAtivadoException(String msg, Throwable t) {
        super(msg, t);
    }
}
