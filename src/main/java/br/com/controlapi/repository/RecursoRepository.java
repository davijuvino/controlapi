package br.com.controlapi.repository;

import br.com.controlapi.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    Optional<Recurso> findByChaveId(String chaveId);

    Optional<Recurso> findByNome(String nome);

    boolean existsByChaveId(String chaveId);

    boolean existsByNome(String nome);
}
