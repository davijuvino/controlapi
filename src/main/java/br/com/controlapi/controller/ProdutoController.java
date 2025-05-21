package br.com.controlapi.controller;

import br.com.controlapi.model.Produto;
import br.com.controlapi.repository.DespesasRepository;
import br.com.controlapi.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DespesasRepository despesasRepository;

    /**
     * Create a new produto
     *
     * @param produto
     * @return ResponseEntity
     */
    @PostMapping("/{despesaId}")
    public Produto create(@PathVariable(value = "despesaId") Long despesaId,
                          @RequestBody Produto produto) {
        return despesasRepository.findById(despesaId).map(expenses -> {
            produto.setDespesas(expenses);
            return produtoRepository.save(produto);
        }).orElseThrow(() -> new IllegalStateException("not found"));
    }
}
