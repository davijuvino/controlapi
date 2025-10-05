package br.com.controlapi.security;

import br.com.controlapi.model.entity.Usuario;
import br.com.controlapi.repository.UsuarioRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UsuarioRepository usuarioRepository;

    public DomainUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return usuarioRepository.findOneWithAuthoritiesByEmail(login)
                .map(usuario -> createSpringSecurityUser(login, usuario))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return usuarioRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(usuario -> createSpringSecurityUser(lowercaseLogin, usuario))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private User createSpringSecurityUser(String lowercaseLogin, Usuario usuario) {
        if (!usuario.isAtivado()) {
            throw new UserNaoAtivadoException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = usuario.getAuthorities().stream()
            .map(autorizacoes -> new SimpleGrantedAuthority(autorizacoes.getNome()))
            .collect(Collectors.toList());
        return new User(usuario.getLogin(),
            usuario.getSenha(),
            grantedAuthorities);
    }
}
