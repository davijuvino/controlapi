package br.com.controlapi.repository;

import br.com.controlapi.model.UserResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResourceRepository extends JpaRepository<UserResource, Long> {

    Optional<UserResource> findByKeyId(String keyId);

    Optional<UserResource> findByName(String name);

    boolean existsByKeyId(String keyId);

    boolean existsByName(String name);
}
