package br.com.controlapi.dto;

import br.com.controlapi.model.Product;
import br.com.controlapi.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExpenseDto {

    private Long id;
    private String name;
    private String category;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime deleteAt;
    private LocalDateTime updateAt;
    private User user;
    private Product product;

    public ExpenseDto(Long id, String name, String category, String description, LocalDateTime createAt, LocalDateTime deleteAt, LocalDateTime updateAt, User user, Product product) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
        this.updateAt = updateAt;
        this.user = user;
        this.product = product;
    }
}
