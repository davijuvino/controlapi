package br.com.controlapi.controller;

import br.com.controlapi.dto.UserDto;
import br.com.controlapi.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {


    @Autowired
    private UserServices userServices;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userServices.getAllUsers();
    }


    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userServices.getUserById(userId);
    }


    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userServices.createUser(userDto), HttpStatus.CREATED);
    }


    @PutMapping("/users/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto){
        return userServices.updateUser(userId, userDto);
    }


    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userServices.deleteUser(userId), HttpStatus.OK);
    }
}
