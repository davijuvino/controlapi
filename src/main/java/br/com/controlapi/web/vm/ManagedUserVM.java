package br.com.controlapi.web.vm;

import br.com.controlapi.model.dto.UserDTO;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String senha;

    @Override
    public String toString() {
        return "ManagedUserVM{" +
                "} " + super.toString();
    }

}
