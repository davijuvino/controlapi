package br.com.controlapi.model;

import br.com.controlapi.dto.UserResourceDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_resource")
public class UserResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "key_id")
    private String keyId;

    public UserResource(UserResourceDto userResourceDto) {
        BeanUtils.copyProperties(userResourceDto, this);
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
        UserResource other = (UserResource) obj;
        return Objects.equals(id, other.id);
    }
    
}
