package br.com.controlapi.controller;

import br.com.controlapi.model.Expenses;
import br.com.controlapi.repository.ExpensesRepository;
import br.com.controlapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new expenses
     *
     * @param expenses
     * @return ResponseEntity
     */
    @PostMapping("/{userId}")
    public Expenses create(@PathVariable (value = "userId") Long userId,
                                           @RequestBody Expenses expenses) {
        return userRepository.findById(userId).map(users -> {
            expenses.setUser(users);
            return expensesRepository.save(expenses);
        }).orElseThrow(() -> new IllegalStateException("not found"));
    }
}
