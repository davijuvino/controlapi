package br.com.controlapi.controller;

import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.services.RecursoServices;
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

public class UsuariosRecursoControllerTest {

    @Mock
    private RecursoServices recursoServices;

    @InjectMocks
    private RecursoController recursoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllResources() {
        // Arrange
        RecursoDto resource1 = new RecursoDto();
        resource1.setId(1L);
        resource1.setNome("Recurso 1");
        List<RecursoDto> expectedResources = List.of(resource1);
        when(recursoServices.buscarTodos()).thenReturn(expectedResources);

        // Act
        List<RecursoDto> actualResources = recursoController.buscarTodos();

        // Assert
        assertEquals(expectedResources, actualResources);
        verify(recursoServices, times(1)).buscarTodos();
    }

    @Test
    public void testGetResourceById() {
        // Arrange

        RecursoDto expectedResource = new RecursoDto();
        expectedResource.setId(1L);
        expectedResource.setNome("Recurso 1");
        when(recursoServices.buscarPorId(1L)).thenReturn(expectedResource);

        // Act
        RecursoDto actualResource = recursoController.buscarPorId(1L);

        // Assert
        assertEquals(expectedResource, actualResource);
        verify(recursoServices, times(1)).buscarPorId(1L);
    }

    @Test
    public void testCriarRecurso() {
        // Arrange

        RecursoDto recursoDto = new RecursoDto();
        recursoDto.setId(null);
        recursoDto.setNome("Recurso 1");
        RecursoDto createdResource = new RecursoDto();
        createdResource.setId(1L);
        createdResource.setNome("Recurso 1");

        when(recursoServices.criar(recursoDto)).thenReturn(createdResource);

        // Act
        ResponseEntity<RecursoDto> response = recursoController.criar(recursoDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdResource, response.getBody());
        verify(recursoServices, times(1)).criar(recursoDto);
    }

    @Test
    public void testAtualizarRecurso() {
        // Arrange
        RecursoDto recursoDto = new RecursoDto();
        recursoDto.setId(1L);
        recursoDto.setNome("Updated Recurso");
        when(recursoServices.atualizar(1L, recursoDto)).thenReturn(recursoDto);

        // Act
        RecursoDto updatedResource = recursoController.atualizar(1L, recursoDto);

        // Assert
        assertEquals(recursoDto, updatedResource);
        verify(recursoServices, times(1)).atualizar(1L, recursoDto);
    }

    @Test
    public void testDeletarRecurso() {
        // Arrange
        String expectedResponse = "Recurso deleted successfully";
        when(recursoServices.delete(1L)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = recursoController.deletar(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(recursoServices, times(1)).delete(1L);
    }
}