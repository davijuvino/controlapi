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
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    private BigDecimal price;
    @Column(name = "date_at")
    private LocalDate dateAt;
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
    @Column(name = "delete_at")
    private LocalDateTime deleteAt;
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    @JsonBackReference
    @JsonProperty("expense_id")
    private Expenses expenses;
}


