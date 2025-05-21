package br.com.controlapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "despesas")
public class Despesas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    @Column(name = "criado_at", nullable = false)
    private LocalDateTime criadoAt;
    @Column(name = "deletado_at")
    private LocalDateTime deletadoAt;
    @Column(name = "atualizado_at")
    private LocalDateTime atualizadoAt;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    @JsonProperty("usuario_id")
    private Usuarios usuarios;

    @OneToMany(mappedBy = "despesas", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Produto> produto = new ArrayList<>();
}


