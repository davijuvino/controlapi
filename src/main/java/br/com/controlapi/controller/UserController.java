package br.com.controlapi.controller;

import br.com.controlapi.model.Users;
import br.com.controlapi.dto.UserDTO;
import br.com.controlapi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;


    /**
     * List of users
     *
     * @return ResponseEntity
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }


    /**
     * Create a new user
     *
     * @param users
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Users> create(@RequestBody Users users) {
        return new ResponseEntity<>(usersRepository.save(users),
                HttpStatus.CREATED);

    }
}
