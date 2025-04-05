package br.com.controlapi.controller;

import br.com.controlapi.model.Product;
import br.com.controlapi.repository.ExpensesRepository;
import br.com.controlapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ExpensesRepository expensesRepository;

    /**
     * Create a new product
     *
     * @param product
     * @return ResponseEntity
     */
    @PostMapping("/{expenseId}")
    public Product create(@PathVariable(value = "expenseId") Long expenseId,
                                           @RequestBody Product product) {
        return expensesRepository.findById(expenseId).map(expenses -> {
            product.setExpenses(expenses);
            return productRepository.save(product);
        }).orElseThrow(() -> new IllegalStateException("not found"));
    }
}
