package br.com.controlapi.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.controlapi.entity.UsuarioVerificadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioVerificadorRepository extends JpaRepository<UsuarioVerificadorEntity, Long>{

	public Optional<UsuarioVerificadorEntity> findByUuid(UUID uuid);
}
