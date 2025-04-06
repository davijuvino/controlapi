package br.com.controlapi.repository;

import br.com.controlapi.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByKey(String key);

    Optional<Resource> findByName(String name);

    boolean existsByKey(String key);

    boolean existsByName(String name);
}
