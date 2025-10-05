package br.com.controlapi.repository;

import java.util.Optional;

import br.com.controlapi.model.entity.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	String USUARIOS_BY_LOGIN_CACHE = "usuariosByLogin";

	String USUARIOS_BY_EMAIL_CACHE = "usuariosByEmail";

	Optional<Usuario> findOneByLogin(String login);

	Optional<Usuario> findOneByEmailIgnoreCase(String email);

	@EntityGraph(attributePaths = "authorities")
	Optional<Usuario> findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	@Cacheable(cacheNames = USUARIOS_BY_LOGIN_CACHE)
	Optional<Usuario> findOneWithAuthoritiesByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	@Cacheable(cacheNames = USUARIOS_BY_EMAIL_CACHE)
	Optional<Usuario> findOneWithAuthoritiesByEmail(String email);
}
