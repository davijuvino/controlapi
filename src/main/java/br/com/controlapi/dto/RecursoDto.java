package br.com.controlapi.dto;

import br.com.controlapi.model.Recurso;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@NoArgsConstructor
//@EqualsAndHashCode(of = "id")
@Getter
@Setter
@ToString
public class RecursoDto {

    private Long id;
    private String nome;
    private String chaveId;

    public RecursoDto(Recurso recurso) {
        BeanUtils.copyProperties(recurso, this);
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
        RecursoDto other = (RecursoDto) obj;
        return Objects.equals(id, other.id);
    }

}
