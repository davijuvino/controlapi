package br.com.controlapi.model.dto;

import br.com.controlapi.config.Constants;
import br.com.controlapi.model.entity.Authority;
import br.com.controlapi.model.entity.User;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

	private Long id;
	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;
	@Size(max = 50)
	private String firstName;
	@Size(max = 50)
	private String lastName;
	@Email
	@Size(min = 5, max = 254)
	private String email;
	private boolean activated = false;
	@Size(min = 2, max = 6)
	private String langKey;
	private String createdBy;
	private Instant createdDate;
	private String lastModifiedBy;
	private Instant lastModifiedDate;
	private Set<String> authorities;
	public UserDTO(User user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.activated = user.isActivated();
		this.langKey = user.getLangKey();
		this.createdBy = user.getCreatedBy();
		this.createdDate = user.getCreatedDate();
		this.lastModifiedBy = user.getLastModifiedBy();
		this.lastModifiedDate = user.getLastModifiedDate();
		this.authorities = user.getAuthorities().stream()
				.map(Authority::getName)
				.collect(Collectors.toSet());
	}
}
