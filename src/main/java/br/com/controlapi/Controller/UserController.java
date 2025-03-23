package br.com.controlapi.Controller;

import br.com.controlapi.model.Users;
import br.com.controlapi.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("control/users")
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Create a new user
     *
     * @param users
     * @return ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody Users users) {
        return new ResponseEntity<>(usersRepository.save(users),
                HttpStatus.CREATED);

    }
}
