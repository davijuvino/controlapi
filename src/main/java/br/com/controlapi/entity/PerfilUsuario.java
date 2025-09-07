package br.com.controlapi.entity;

import br.com.controlapi.dto.PerfilUsuarioDTO;
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

@Entity
@Table(name = "NPL_PERFIL_USUARIO")
@Getter
@Setter
@NoArgsConstructor
public class PerfilUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "ID_PERFIL")
	private Perfil perfil;
	
	public PerfilUsuario(PerfilUsuarioDTO perfilUsuario) {
		BeanUtils.copyProperties(perfilUsuario, this);
		if(perfilUsuario != null && perfilUsuario.getUsuario() != null) {
			this.usuario = new Usuario(perfilUsuario.getUsuario());
		}
		if(perfilUsuario != null && perfilUsuario.getPerfil() != null) {
			this.perfil = new Perfil(perfilUsuario.getPerfil());
		}	
	}
	
}
