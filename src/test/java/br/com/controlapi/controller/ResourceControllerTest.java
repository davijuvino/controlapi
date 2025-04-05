package br.com.controlapi.controller;

import br.com.controlapi.dto.ResourceDto;
import br.com.controlapi.services.ResourceServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ResourceControllerTest {

    @Mock
    private ResourceServices resourceServices;

    @InjectMocks
    private ResourceController resourceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllResources() {
        // Arrange
        ResourceDto resource1 = new ResourceDto();
        resource1.setId(1L);
        resource1.setName("Resource 1");
        List<ResourceDto> expectedResources = List.of(resource1);
        when(resourceServices.getAllResources()).thenReturn(expectedResources);

        // Act
        List<ResourceDto> actualResources = resourceController.getAllResources();

        // Assert
        assertEquals(expectedResources, actualResources);
        verify(resourceServices, times(1)).getAllResources();
    }

    @Test
    public void testGetResourceById() {
        // Arrange

        ResourceDto expectedResource = new ResourceDto();
        expectedResource.setId(1L);
        expectedResource.setName("Resource 1");
        when(resourceServices.getResourceById(1L)).thenReturn(expectedResource);

        // Act
        ResourceDto actualResource = resourceController.getResourceById(1L);

        // Assert
        assertEquals(expectedResource, actualResource);
        verify(resourceServices, times(1)).getResourceById(1L);
    }

    @Test
    public void testCreateResource() {
        // Arrange

        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(null);
        resourceDto.setName("Resource 1");
        ResourceDto createdResource = new ResourceDto();
        createdResource.setId(1L);
        createdResource.setName("Resource 1");

        when(resourceServices.createResource(resourceDto)).thenReturn(createdResource);

        // Act
        ResponseEntity<ResourceDto> response = resourceController.createResource(resourceDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdResource, response.getBody());
        verify(resourceServices, times(1)).createResource(resourceDto);
    }

    @Test
    public void testUpdateResource() {
        // Arrange
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(1L);
        resourceDto.setName("Updated Resource");
        when(resourceServices.updateResource(1L, resourceDto)).thenReturn(resourceDto);

        // Act
        ResourceDto updatedResource = resourceController.updateResource(1L, resourceDto);

        // Assert
        assertEquals(resourceDto, updatedResource);
        verify(resourceServices, times(1)).updateResource(1L, resourceDto);
    }

    @Test
    public void testDeleteResource() {
        // Arrange
        String expectedResponse = "Resource deleted successfully";
        when(resourceServices.deleteResource(1L)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = resourceController.deleteResource(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(resourceServices, times(1)).deleteResource(1L);
    }
}