package br.com.controlapi.service;

import java.util.List;

import br.com.controlapi.dto.PermissaoPerfilRecursoDTO;
import br.com.controlapi.entity.PermissaoPerfilRecurso;
import br.com.controlapi.repository.PermissaoPerfilRecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PermissaoPerfilRecursoService {

	@Autowired
	private PermissaoPerfilRecursoRepository permissaoPerfilRecursoRepository;
	
	public List<PermissaoPerfilRecursoDTO> listarTodos(){
		List<PermissaoPerfilRecurso> permissaoPerfilRecusos = permissaoPerfilRecursoRepository.findAll();
		return permissaoPerfilRecusos.stream().map(PermissaoPerfilRecursoDTO::new).toList();
	}
	
	public void inserir(PermissaoPerfilRecursoDTO permissaoPerfilRecurso) {
		PermissaoPerfilRecurso permissaoPerfilRecursoEntity = new PermissaoPerfilRecurso(permissaoPerfilRecurso);
		permissaoPerfilRecursoRepository.save(permissaoPerfilRecursoEntity);
	}
	
	public PermissaoPerfilRecursoDTO alterar(PermissaoPerfilRecursoDTO permissaoPerfilRecurso) {
		PermissaoPerfilRecurso permissaoPerfilRecursoEntity = new PermissaoPerfilRecurso(permissaoPerfilRecurso);
		return new PermissaoPerfilRecursoDTO(permissaoPerfilRecursoRepository.save(permissaoPerfilRecursoEntity));
	}
	
	public void excluir(Long id) {
		PermissaoPerfilRecurso permissaoPerfilRecurso = permissaoPerfilRecursoRepository.findById(id).get();
		permissaoPerfilRecursoRepository.delete(permissaoPerfilRecurso);
	}
	
	public PermissaoPerfilRecursoDTO buscarPorId(Long id) {
		return new PermissaoPerfilRecursoDTO(permissaoPerfilRecursoRepository.findById(id).get());
	}
}
