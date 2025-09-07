package br.com.controlapi.repository;

import java.util.Optional;

import br.com.controlapi.entity.UsuarioVerificador;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioVerificadorRepository extends JpaRepository<UsuarioVerificador, Long>{

	public Optional<UsuarioVerificador> findByUuid(String uuid);
}
