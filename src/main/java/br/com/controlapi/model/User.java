package br.com.controlapi.model;

import br.com.controlapi.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(exclude = {"expenses"}) //Para trabalhar com usuarios associados, devemos excluir hashcode
@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String login;
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

    public User(UserDto userDto){
        BeanUtils.copyProperties(userDto, this);
    }

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}

