package br.com.controlapi.repository;

import java.util.Optional;

import br.com.controlapi.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{

	Optional<UsuarioEntity> findByLogin(String login);
}
