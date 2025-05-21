package br.com.controlapi.dto;

import br.com.controlapi.model.Produto;
import br.com.controlapi.model.Usuarios;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DespesasDto {

    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private LocalDateTime criadoAt;
    private LocalDateTime deletadoAt;
    private LocalDateTime atualizadoAt;
    private Usuarios usuarios;
    private Produto produto;

    public DespesasDto(Long id, String nome, String categoria, String descricao, LocalDateTime criadoAt, LocalDateTime deletadoAt, LocalDateTime atualizadoAt, Usuarios usuarios, Produto produto) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.criadoAt = criadoAt;
        this.deletadoAt = deletadoAt;
        this.atualizadoAt = atualizadoAt;
        this.usuarios = usuarios;
        this.produto = produto;
    }
}
