package br.com.controlapi.controller;

import br.com.controlapi.dto.AutenticacaoDTO;
import br.com.controlapi.dto.UsuarioDTO;
import br.com.controlapi.service.AutenticacaoService;
import br.com.controlapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/autenticacao")
@CrossOrigin
public class AutenticacaoController {

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody AutenticacaoDTO authDto){
		return ResponseEntity.ok(autenticacaoService.login(authDto));
	}
	
	@PostMapping(value = "/novoUsuario")
	public void inserirNovoUsuario(@RequestBody UsuarioDTO novoUsuario){
		usuarioService.inserirNovoUsuario(novoUsuario);
	}
	
	@GetMapping(value = "/verificarCadastro/{uuid}")
	public String verificarCadastro(@PathVariable("uuid") String uuid) {
		return usuarioService.verificarCadastro(uuid);
	}
	
}
