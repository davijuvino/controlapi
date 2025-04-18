package br.com.controlapi.dto;

import br.com.controlapi.model.UserProfile;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {

	private Long id;
	private UserDto user;
	private ProfileDto profile;

	public UserProfileDto(UserProfile userProfile) {
		BeanUtils.copyProperties(userProfile, this);
		this.user = Optional.ofNullable(userProfile.getUser()).map(UserDto::new)
				.orElse(null);
		this.profile = Optional.ofNullable(userProfile.getProfile()).map(ProfileDto::new)
				.orElse(null);
	}
	
}
