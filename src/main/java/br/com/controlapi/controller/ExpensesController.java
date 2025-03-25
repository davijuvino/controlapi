package br.com.controlapi.controller;

import br.com.controlapi.model.Expenses;
import br.com.controlapi.repository.ExpensesRepository;
import br.com.controlapi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Create a new expenses
     *
     * @param expenses
     * @return ResponseEntity
     */
    @PostMapping("/{userId}")
    public Expenses create(@PathVariable (value = "userId") Long userId,
                                           @RequestBody Expenses expenses) {
        return usersRepository.findById(userId).map(users -> {
            expenses.setUser(users);
            return expensesRepository.save(expenses);
        }).orElseThrow(() -> new IllegalStateException("not found"));
    }
}
