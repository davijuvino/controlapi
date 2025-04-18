package br.com.controlapi.dto;

import org.springframework.beans.BeanUtils;
import br.com.controlapi.model.Profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDto {

	private Long id;
	private String description;
	
	public ProfileDto(Profile profile) {
		BeanUtils.copyProperties(profile, this);
	}
}
