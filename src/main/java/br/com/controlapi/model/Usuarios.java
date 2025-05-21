package br.com.controlapi.model;

import br.com.controlapi.dto.UsuariosDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(exclude = {"despesas"}) //Para trabalhar com usuarios associados, devemos excluir hashcode
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    @Column(name = "criado_at", nullable = false)
    private LocalDateTime criadoAt;
    @Column(name = "deletado_at")
    private LocalDateTime deletadoAt;
    @Column(name = "atualizado_at")
    private LocalDateTime atualizadoAt;
    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Despesas> despesas = new ArrayList<>();

    public Usuarios(UsuariosDto usuariosDto){
        BeanUtils.copyProperties(usuariosDto, this);
    }

    @PrePersist
    protected void onCriado() {
        this.criadoAt = LocalDateTime.now();
    }
}

