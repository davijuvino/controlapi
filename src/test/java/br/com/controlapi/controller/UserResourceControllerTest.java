package br.com.controlapi.controller;

import br.com.controlapi.dto.UserResourceDto;
import br.com.controlapi.services.UserResourceServices;
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

public class UserResourceControllerTest {

    @Mock
    private UserResourceServices userResourceServices;

    @InjectMocks
    private UserResourceController userResourceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllResources() {
        // Arrange
        UserResourceDto resource1 = new UserResourceDto();
        resource1.setId(1L);
        resource1.setName("UserResource 1");
        List<UserResourceDto> expectedResources = List.of(resource1);
        when(userResourceServices.getAllResources()).thenReturn(expectedResources);

        // Act
        List<UserResourceDto> actualResources = userResourceController.getAllResources();

        // Assert
        assertEquals(expectedResources, actualResources);
        verify(userResourceServices, times(1)).getAllResources();
    }

    @Test
    public void testGetResourceById() {
        // Arrange

        UserResourceDto expectedResource = new UserResourceDto();
        expectedResource.setId(1L);
        expectedResource.setName("UserResource 1");
        when(userResourceServices.getResourceById(1L)).thenReturn(expectedResource);

        // Act
        UserResourceDto actualResource = userResourceController.getResourceById(1L);

        // Assert
        assertEquals(expectedResource, actualResource);
        verify(userResourceServices, times(1)).getResourceById(1L);
    }

    @Test
    public void testCreateResource() {
        // Arrange

        UserResourceDto userResourceDto = new UserResourceDto();
        userResourceDto.setId(null);
        userResourceDto.setName("UserResource 1");
        UserResourceDto createdResource = new UserResourceDto();
        createdResource.setId(1L);
        createdResource.setName("UserResource 1");

        when(userResourceServices.createResource(userResourceDto)).thenReturn(createdResource);

        // Act
        ResponseEntity<UserResourceDto> response = userResourceController.createResource(userResourceDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdResource, response.getBody());
        verify(userResourceServices, times(1)).createResource(userResourceDto);
    }

    @Test
    public void testUpdateResource() {
        // Arrange
        UserResourceDto userResourceDto = new UserResourceDto();
        userResourceDto.setId(1L);
        userResourceDto.setName("Updated UserResource");
        when(userResourceServices.updateResource(1L, userResourceDto)).thenReturn(userResourceDto);

        // Act
        UserResourceDto updatedResource = userResourceController.updateResource(1L, userResourceDto);

        // Assert
        assertEquals(userResourceDto, updatedResource);
        verify(userResourceServices, times(1)).updateResource(1L, userResourceDto);
    }

    @Test
    public void testDeleteResource() {
        // Arrange
        String expectedResponse = "UserResource deleted successfully";
        when(userResourceServices.deleteResource(1L)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = userResourceController.deleteResource(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userResourceServices, times(1)).deleteResource(1L);
    }
}