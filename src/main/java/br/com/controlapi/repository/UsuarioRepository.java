package br.com.controlapi.repository;

import java.util.Optional;

import br.com.controlapi.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByLogin(String login);
}
