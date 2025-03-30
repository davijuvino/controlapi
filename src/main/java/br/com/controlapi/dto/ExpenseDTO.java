package br.com.controlapi.dto;

import br.com.controlapi.model.Expenses;
import br.com.controlapi.model.Product;
import br.com.controlapi.model.Users;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExpenseDTO {

    private Long id;
    private String name;
    private String category;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime deleteAt;
    private LocalDateTime updateAt;
    private Users users;
    private Product product;

    public ExpenseDTO(Long id, String name, String category, String description, LocalDateTime createAt, LocalDateTime deleteAt, LocalDateTime updateAt, Users users, Product product) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
        this.updateAt = updateAt;
        this.users = users;
        this.product = product;
    }
}
