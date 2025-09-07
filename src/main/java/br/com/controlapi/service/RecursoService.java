package br.com.controlapi.service;

import java.util.List;

import br.com.controlapi.dto.RecursoDTO;
import br.com.controlapi.entity.Recurso;
import br.com.controlapi.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RecursoService {

	@Autowired
	private RecursoRepository recursoRepository;
	
	public List<RecursoDTO> listarTodos(){
		List<Recurso> recursos = recursoRepository.findAll();
		return recursos.stream().map(RecursoDTO::new).toList();
	}
	
	public void inserir(RecursoDTO recurso) {
		Recurso recursoEntity = new Recurso(recurso);
		recursoRepository.save(recursoEntity);
	}
	
	public RecursoDTO alterar(RecursoDTO recurso) {
		Recurso recursoEntity = new Recurso(recurso);
		return new RecursoDTO(recursoRepository.save(recursoEntity));
	}
	
	public void excluir(Long id) {
		Recurso recurso = recursoRepository.findById(id).get();
		recursoRepository.delete(recurso);
	}
	
	public RecursoDTO buscarPorId(Long id) {
		return new RecursoDTO(recursoRepository.findById(id).get());
	}
}
