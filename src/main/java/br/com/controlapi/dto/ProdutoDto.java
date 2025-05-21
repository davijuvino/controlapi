package br.com.controlapi.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProdutoDto {

    private Long id;
    private Integer quantidade;
    private BigDecimal preco;
    private LocalDate dataAt;
    private LocalDateTime criadoAt;
    private LocalDateTime deletadoAt;
    private LocalDateTime atualizadoAt;
    private Long despesaId; // Representa o ID da despesa associada
}
