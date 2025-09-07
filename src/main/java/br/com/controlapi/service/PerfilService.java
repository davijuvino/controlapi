package br.com.controlapi.service;

import java.util.List;

import br.com.controlapi.dto.PerfilDTO;
import br.com.controlapi.entity.Perfil;
import br.com.controlapi.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class PerfilService {

	@Autowired
	private PerfilRepository perfilRepository;
	
	public List<PerfilDTO> listarTodos(){
		List<Perfil> perfis = perfilRepository.findAll();
		return perfis.stream().map(PerfilDTO::new).toList();
	}
	
	public void inserir(PerfilDTO perfil) {
		Perfil perfilEntity = new Perfil(perfil);
		perfilRepository.save(perfilEntity);
	}
	
	public PerfilDTO alterar(PerfilDTO perfil) {
		Perfil perfilEntity = new Perfil(perfil);
		return new PerfilDTO(perfilRepository.save(perfilEntity));
	}
	
	public void excluir(Long id) {
		Perfil perfil = perfilRepository.findById(id).get();
		perfilRepository.delete(perfil);
	}
	
	public PerfilDTO buscarPorId(Long id) {
		return new PerfilDTO(perfilRepository.findById(id).get());
	}
}
