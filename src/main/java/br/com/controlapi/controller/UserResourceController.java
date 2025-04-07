package br.com.controlapi.controller;

import br.com.controlapi.dto.UserResourceDto;
import br.com.controlapi.services.UserResourceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserResourceController {

    @Autowired
    private UserResourceServices userResourceServices;

    @GetMapping("/resources")
    public List<UserResourceDto> getAllResources() {
        return userResourceServices.getAllResources();
    }

    @GetMapping("/resources/{resourceId}")
    public UserResourceDto getResourceById(@PathVariable Long resourceId) {
        return userResourceServices.getResourceById(resourceId);
    }

    @PostMapping("/resources")
    public ResponseEntity<UserResourceDto> createResource(@RequestBody UserResourceDto userResourceDto) {
        return new ResponseEntity<>(userResourceServices.createResource(userResourceDto), HttpStatus.CREATED);
    }

    @PutMapping("/resources/{resourceId}")
    public UserResourceDto updateResource(@PathVariable Long resourceId, @RequestBody UserResourceDto userResourceDto) {
        return userResourceServices.updateResource(resourceId, userResourceDto);
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<String> deleteResource(@PathVariable Long resourceId) {
        return new ResponseEntity<>(userResourceServices.deleteResource(resourceId), HttpStatus.OK);
    }
}