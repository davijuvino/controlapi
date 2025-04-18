package br.com.controlapi.model;

import br.com.controlapi.dto.ResourcePermissionProfileDto;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name = "npl_resource_permission_profile")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ResourcePermissionProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
	
	@ManyToOne
	@JoinColumn(name = "resource_id")
	private UserResource userResource;

	public ResourcePermissionProfile(ResourcePermissionProfileDto resourcePermissionProfileDto) {
		BeanUtils.copyProperties(resourcePermissionProfileDto, this);
		this.userResource = Optional.ofNullable(
				resourcePermissionProfileDto.getUserResource()).map(UserResource::new)
				.orElse(null);
		this.profile = Optional.ofNullable(
				resourcePermissionProfileDto.getProfile()).map(Profile::new)
				.orElse(null);
	}
}
