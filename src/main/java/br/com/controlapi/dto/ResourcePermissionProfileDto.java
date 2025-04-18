package br.com.controlapi.dto;

import br.com.controlapi.model.ResourcePermissionProfile;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class ResourcePermissionProfileDto {
	
	private Long id;
	private ProfileDto profile;
	private UserResourceDto userResource;
	/*
	public ResourcePermissionProfileDto(ResourcePermissionProfile resourcePermissionProfile) {
		BeanUtils.copyProperties(resourcePermissionProfile, this);
		if(resourcePermissionProfile.getUserResource() != null) {
			this.userResource = new UserResourceDto(resourcePermissionProfile.getUserResource());
		}
		if(resourcePermissionProfile.getProfile() != null) {
			this.profile = new ProfileDto(resourcePermissionProfile.getProfile());
		}
	}*/

	public ResourcePermissionProfileDto(ResourcePermissionProfile resourcePermissionProfile) {
		BeanUtils.copyProperties(resourcePermissionProfile, this);
		this.userResource = Optional.ofNullable(resourcePermissionProfile.getUserResource())
				.map(UserResourceDto::new)
				.orElse(null);
		this.profile = Optional.ofNullable(resourcePermissionProfile.getProfile())
				.map(ProfileDto::new)
				.orElse(null);
	}
}
