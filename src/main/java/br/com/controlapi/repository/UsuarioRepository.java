package br.com.controlapi.repository;
import br.com.controlapi.model.Usuarios;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {
    boolean existsByEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") String email);
}
