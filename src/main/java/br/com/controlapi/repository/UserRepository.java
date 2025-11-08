package br.com.controlapi.repository;

import java.util.Optional;

import br.com.controlapi.model.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	String USERS_BY_LOGIN_CACHE = "usersByLogin";

	String USERS_BY_EMAIL_CACHE = "usersByEmail";

	Optional<User> findOneByActivationKey(String activationKey);

	Optional<User> findOneByResetKey(String resetKey);

	Optional<User> findOneByLogin(String login);

	Optional<User> findOneByEmailIgnoreCase(String email);

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesById(Long id);

	@EntityGraph(attributePaths = "authorities")
	@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
	Optional<User> findOneWithAuthoritiesByLogin(String login);

	@EntityGraph(attributePaths = "authorities")
	@Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
	Optional<User> findOneWithAuthoritiesByEmail(String email);
}
