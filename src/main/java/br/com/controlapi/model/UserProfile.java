package br.com.controlapi.model;

import br.com.controlapi.dto.UserProfileDto;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name = "npl_user_profile")
@Getter
@Setter
@NoArgsConstructor
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;

	public UserProfile(UserProfileDto userProfileDto) {
		BeanUtils.copyProperties(userProfileDto, this);
		this.user = Optional.ofNullable(userProfileDto.getUser())
				.map(User::new)
				.orElse(null);
		this.profile = Optional.ofNullable(userProfileDto.getProfile())
				.map(Profile::new)
				.orElse(null);
	}
	
}
