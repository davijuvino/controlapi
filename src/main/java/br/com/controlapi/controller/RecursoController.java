package br.com.controlapi.controller;

import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.services.RecursoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecursoController {

    @Autowired
    private RecursoServices recursoServices;

    @GetMapping("/recursos")
    public List<RecursoDto> getAllResources() {
        return recursoServices.getAllRecursos();
    }

    @GetMapping("/recursos/{recursoId}")
    public RecursoDto getResourceById(@PathVariable Long recursoId) {
        return recursoServices.getRecursoById(recursoId);
    }

    @PostMapping("/recursos")
    public ResponseEntity<RecursoDto> criarRecurso(@RequestBody RecursoDto recursoDto) {
        return new ResponseEntity<>(recursoServices.criarRecurso(recursoDto), HttpStatus.CREATED);
    }

    @PutMapping("/recursos/{recursoId}")
    public RecursoDto atualizarRecurso(@PathVariable Long resourceId, @RequestBody RecursoDto recursoDto) {
        return recursoServices.atualizarRecurso(resourceId, recursoDto);
    }

    @DeleteMapping("/recursos/{recursoId}")
    public ResponseEntity<String> deletarRecurso(@PathVariable Long recursoId) {
        return new ResponseEntity<>(recursoServices.deletarRecurso(recursoId), HttpStatus.OK);
    }
}