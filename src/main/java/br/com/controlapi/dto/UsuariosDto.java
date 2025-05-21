package br.com.controlapi.dto;

import br.com.controlapi.model.Despesas;
import br.com.controlapi.model.Usuarios;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UsuariosDto {

    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String nome;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    private String login;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String senha;
    @CreatedDate
    private LocalDateTime criadoAt;
    private LocalDateTime deletadoAt;
    private LocalDateTime atualizadoAt;
    private List<Despesas> despesas;

    public UsuariosDto(Usuarios usuarios){
        BeanUtils.copyProperties(usuarios, this);
    }
}
