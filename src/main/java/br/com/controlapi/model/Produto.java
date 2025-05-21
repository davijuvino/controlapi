package br.com.controlapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantidade;
    private BigDecimal preco;
    @Column(name = "data_at")
    private LocalDate dataAt;
    @Column(name = "criado_at", nullable = false)
    private LocalDateTime criadoAt;
    @Column(name = "deletado_at")
    private LocalDateTime deletadoAt;
    @Column(name = "atualizado_at")
    private LocalDateTime atualizadoAt;

    @ManyToOne
    @JoinColumn(name = "despesa_id", nullable = false)
    @JsonBackReference
    @JsonProperty("despesa_id")
    private Despesas despesas;
}


