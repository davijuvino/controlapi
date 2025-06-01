package br.com.controlapi.controller;

import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.services.RecursoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recursos")
public class RecursoController {

    @Autowired
    private RecursoServices recursoServices;

    @GetMapping
    public List<RecursoDto> buscarTodos() {
        return recursoServices.buscarTodos();
    }

    @GetMapping("/{recursoId}")
    public RecursoDto buscarPorId(@PathVariable Long recursoId) {
        return recursoServices.buscarPorId(recursoId);
    }

    @PostMapping
    public ResponseEntity<RecursoDto> criar(@RequestBody RecursoDto recursoDto) {
        return new ResponseEntity<>(recursoServices.criar(recursoDto), HttpStatus.CREATED);
    }

    @PutMapping("/{recursoId}")
    public RecursoDto atualizar(@PathVariable Long recursoId, @RequestBody RecursoDto recursoDto) {
        return recursoServices.atualizar(recursoId, recursoDto);
    }

    @DeleteMapping("/{recursoId}")
    public ResponseEntity<String> deletar(@PathVariable Long recursoId) {
        return new ResponseEntity<>(recursoServices.delete(recursoId), HttpStatus.OK);
    }
}