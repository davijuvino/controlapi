package br.com.controlapi.dto;

import br.com.controlapi.model.UserResource;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
@Getter
@Setter
@ToString
public class UserResourceDto {

    private Long id;
    private String name;
    private String keyId;

    public UserResourceDto(UserResource userResource) {
        BeanUtils.copyProperties(userResource, this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserResourceDto other = (UserResourceDto) obj;
        return Objects.equals(id, other.id);
    }

}
