package br.com.controlapi.web.vm;

import br.com.controlapi.model.dto.UsuarioDTO;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GerenciarUsuarioVM extends UsuarioDTO {

    public static final int SENHA_MIN_LENGTH = 4;

    public static final int SENHA_MAX_LENGTH = 100;

    @Size(min = SENHA_MIN_LENGTH, max = SENHA_MAX_LENGTH)
    private String senha;

    @Override
    public String toString() {
        return "GerenciarUsuarioVM{" +
                "} " + super.toString();
    }

}
