package br.com.controlapi.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;
    private Integer amount;
    private BigDecimal price;
    private LocalDate dateAt;
    private LocalDateTime createAt;
    private LocalDateTime deleteAt;
    private LocalDateTime updateAt;
    private Long expenseId; // Representa o ID da despesa associada
}
