package br.com.controlapi.controller;

import br.com.controlapi.model.Despesas;
import br.com.controlapi.repository.DespesasRepository;
import br.com.controlapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/despesas")
public class DespesasController {

    @Autowired
    private DespesasRepository despesasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Create a new despesas
     *
     * @param despesas
     * @return ResponseEntity
     */
    @PostMapping("/{usuarioId}")
    public Despesas create(@PathVariable (value = "usuarioId") Long usuarioId,
                           @RequestBody Despesas despesas) {
        return usuarioRepository.findById(usuarioId).map(users -> {
            despesas.setUsuarios(users);
            return despesasRepository.save(despesas);
        }).orElseThrow(() -> new IllegalStateException("not found"));
    }
}
