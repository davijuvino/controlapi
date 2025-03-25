package br.com.controlapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"expenses"}) //Para trabalhar com usuarios associados, devemos excluir hashcode
@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
    @Column(name = "delete_at")
    private LocalDateTime deleteAt;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Expenses> expenses = new ArrayList<>();
}

