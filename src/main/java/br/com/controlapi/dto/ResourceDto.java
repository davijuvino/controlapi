package br.com.controlapi.dto;

import lombok.*;
import org.springframework.beans.BeanUtils;
import br.com.controlapi.model.Resource;

import java.util.Objects;

@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
@Getter
@Setter
@ToString
public class ResourceDto {

    private Long id;
    private String name;
    private String key;

    public ResourceDto(Resource resource) {
        BeanUtils.copyProperties(resource, this);
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
        ResourceDto other = (ResourceDto) obj;
        return Objects.equals(id, other.id);
    }

}
