package br.com.controlapi.controller;


import br.com.controlapi.dto.PaginacaoDTO;
import br.com.controlapi.dto.UsuarioDTO;
import br.com.controlapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/usuarios")
@CrossOrigin
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<PaginacaoDTO<UsuarioDTO>> listarTodos(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "nome") String sort) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		PaginacaoDTO<UsuarioDTO> resultado = usuarioService.listarTodos(pageable);

		return ResponseEntity.ok(resultado);
	}

	@PostMapping
	public void inserir(@RequestBody UsuarioDTO usuario) {
		usuarioService.inserir(usuario);
	}
	
	@PutMapping("/{id}")
	public UsuarioDTO alterar(@RequestBody UsuarioDTO usuario, @PathVariable("id") Long id) {
		return usuarioService.alterar(usuario, id);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable("id") Long id){
		usuarioService.excluir(id);
		return ResponseEntity.ok().build();
	}
}
