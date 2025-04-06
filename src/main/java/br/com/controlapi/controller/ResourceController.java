package br.com.controlapi.controller;

import br.com.controlapi.dto.ResourceDto;
import br.com.controlapi.services.ResourceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResourceController {

    @Autowired
    private ResourceServices resourceServices;

    @GetMapping("/resources")
    public List<ResourceDto> getAllResources() {
        return resourceServices.getAllResources();
    }

    @GetMapping("/resources/{resourceId}")
    public ResourceDto getResourceById(@PathVariable Long resourceId) {
        return resourceServices.getResourceById(resourceId);
    }

    @PostMapping("/resources")
    public ResponseEntity<ResourceDto> createResource(@RequestBody ResourceDto resourceDto) {
        return new ResponseEntity<>(resourceServices.createResource(resourceDto), HttpStatus.CREATED);
    }

    @PutMapping("/resources/{resourceId}")
    public ResourceDto updateResource(@PathVariable Long resourceId, @RequestBody ResourceDto resourceDto) {
        return resourceServices.updateResource(resourceId, resourceDto);
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<String> deleteResource(@PathVariable Long resourceId) {
        return new ResponseEntity<>(resourceServices.deleteResource(resourceId), HttpStatus.OK);
    }
}