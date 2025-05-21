package br.com.controlapi.controller;

import br.com.controlapi.dto.UsuariosDto;
import br.com.controlapi.services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {


    @Autowired
    private UsuarioServices usuarioServices;

    @GetMapping("/usuarios")
    public List<UsuariosDto> getAllUsers() {
        return usuarioServices.getAllUsers();
    }


    @GetMapping("/usuarios/{usuarioId}")
    public UsuariosDto getUserById(@PathVariable Long usuarioId) {
        return usuarioServices.getUserById(usuarioId);
    }


    @PostMapping("/usuarios")
    public ResponseEntity<UsuariosDto> criarUsuario(@RequestBody UsuariosDto usuariosDto) {
        return new ResponseEntity<>(usuarioServices.createUser(usuariosDto), HttpStatus.CREATED);
    }


    @PutMapping("/usuarios/{usuarioId}")
    public UsuariosDto atualizarUsuario(@PathVariable Long usuarioId, @RequestBody UsuariosDto usuariosDto){
        return usuarioServices.updateUser(usuarioId, usuariosDto);
    }


    @DeleteMapping("/usuarios/{usuarioId}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long usuarioId) {
        return new ResponseEntity<>(usuarioServices.deleteUser(usuarioId), HttpStatus.OK);
    }
}
