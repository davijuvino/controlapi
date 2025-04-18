package br.com.controlapi.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "npl_verifying_user")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class VerifyingUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private UUID uuid;
	
	@Column(name = "expiration_date", nullable = false)
	private Instant expirationDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
	private User user;
}
