package br.com.controlapi.controller;

import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.services.UsuarioServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServices usuarioServices;

    @GetMapping
    public ResponseEntity<List<UsuariosDto>> listar() {
        List<UsuariosDto> usuarios = usuarioServices.listar();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDto> buscarPorId(@PathVariable Long id) {
        UsuariosDto usuario = usuarioServices.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuariosDto> criar(@RequestBody UsuariosDto usuariosDto) {
        UsuariosDto usuarioCriado = usuarioServices.criar(usuariosDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuariosDto> atualizar(
            @PathVariable Long id,
            @RequestBody UsuariosDto usuariosDto) {
        UsuariosDto usuarioAtualizado = usuarioServices.atualizar(id, usuariosDto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        usuarioServices.deletar(id);
    }
}