package br.com.controlapi.model;

import br.com.controlapi.dto.PerfilPermissaoDto;
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
@Table(name = "npl_perfil_permissao")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PerfilPermissao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;
	
	@ManyToOne
	@JoinColumn(name = "recurso_id")
	private Recurso recurso;

	public PerfilPermissao(PerfilPermissaoDto perfilPermissaoDto) {
		BeanUtils.copyProperties(perfilPermissaoDto, this);
		this.recurso = Optional.ofNullable(
				perfilPermissaoDto.getRecurso()).map(Recurso::new)
				.orElse(null);
		this.perfil = Optional.ofNullable(
				perfilPermissaoDto.getPerfil()).map(Perfil::new)
				.orElse(null);
	}
}
