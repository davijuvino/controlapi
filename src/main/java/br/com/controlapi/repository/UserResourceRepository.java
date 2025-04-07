package br.com.controlapi.repository;

import br.com.controlapi.model.UserResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResourceRepository extends JpaRepository<UserResource, Long> {

    Optional<UserResource> findByKey(String key);

    Optional<UserResource> findByName(String name);

    boolean existsByKey(String key);

    boolean existsByName(String name);
}
