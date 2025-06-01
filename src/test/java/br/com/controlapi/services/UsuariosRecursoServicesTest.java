package br.com.controlapi.services;

import br.com.controlapi.constants.Mensagem;
import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.exception.CriacaoException;
import br.com.controlapi.exception.NaoEncontradoException;
import br.com.controlapi.model.Recurso;
import br.com.controlapi.repository.RecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuariosRecursoServicesTest {

    @Mock
    private RecursoRepository RecursoRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private RecursoServices recursoServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar() {
        RecursoDto recursoDto = new RecursoDto();
        recursoDto.setChaveId("testKey");
        Recurso recurso = new Recurso(recursoDto);
        when(RecursoRepository.existsByChaveId(recursoDto.getChaveId())).thenReturn(false);
        when(RecursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        RecursoDto result = recursoServices.criar(recursoDto);

        assertEquals(recursoDto.getChaveId(), result.getChaveId());
        verify(RecursoRepository, times(1)).existsByChaveId(recursoDto.getChaveId());
        verify(RecursoRepository, times(1)).save(any(Recurso.class));
    }

    @Test
    void criar_ThrowsException_WhenKeyExists() {
        RecursoDto recursoDto = new RecursoDto();
        recursoDto.setChaveId("testKey");
        when(RecursoRepository.existsByChaveId(recursoDto.getChaveId())).thenReturn(true);

        assertThrows(CriacaoException.class, () -> recursoServices.criar(recursoDto));
        verify(RecursoRepository, times(1)).existsByChaveId(recursoDto.getChaveId());
        verify(RecursoRepository, never()).save(any(Recurso.class));
    }

    @Test
    void getAllResources_ReturnsListOfRecursos() {
        List<Recurso> recursos = Arrays.asList(new Recurso(), new Recurso());
        when(RecursoRepository.findAll()).thenReturn(recursos);

        List<RecursoDto> result = recursoServices.buscarTodos();

        assertEquals(recursos.size(), result.size());
        verify(RecursoRepository, times(1)).findAll();
    }

    @Test
    void getResourceById_ReturnsRecurso() {
        Recurso recurso = new Recurso();
        when(RecursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        RecursoDto result = recursoServices.buscarPorId(1L);

        assertNotNull(result);
        verify(RecursoRepository, times(1)).findById(1L);
    }

    @Test
    void getResourceById_ThrowsException_WhenRecursoNotFound() {
        when(RecursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> recursoServices.buscarPorId(1L));
        verify(RecursoRepository, times(1)).findById(1L);
    }

    @Test
    void atualizar() {
        RecursoDto recursoDto = new RecursoDto();
        Recurso recurso = new Recurso();
        when(RecursoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(RecursoRepository.save(any(Recurso.class))).thenReturn(recurso);

        RecursoDto result = recursoServices.atualizar(1L, recursoDto);

        assertNotNull(result);
        verify(RecursoRepository, times(1)).findById(1L);
        verify(RecursoRepository, times(1)).save(any(Recurso.class));
    }

    @Test
    void atualizarNotFound() {
        RecursoDto recursoDto = new RecursoDto();
        when(RecursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> recursoServices.atualizar(1L, recursoDto));
        verify(RecursoRepository, times(1)).findById(1L);
        verify(RecursoRepository, never()).save(any(Recurso.class));
    }

    @Test
    void delete() {
        Recurso recurso = new Recurso();
        when(RecursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        String result = recursoServices.delete(1L);

        assertEquals(Mensagem.DELETE_OK, result);
        verify(RecursoRepository, times(1)).findById(1L);
        verify(RecursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFound() {
        when(RecursoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NaoEncontradoException.class, () -> recursoServices.delete(1L));
        verify(RecursoRepository, times(1)).findById(1L);
        verify(RecursoRepository, never()).deleteById(1L);
    }

    @Test
    void atualizar_WhenGivenValidInputAndExistingId() {
        // Arrange
        Long resourceId = 1L;
        RecursoDto recursoDto = new RecursoDto();
        recursoDto.setChaveId("updatedKey");
        recursoDto.setNome("updatedValue");


        Recurso existingRecurso = new Recurso();
        existingRecurso.setId(resourceId);
        existingRecurso.setChaveId("oldKey");
        existingRecurso.setNome("oldValue");

        when(RecursoRepository.findById(resourceId)).thenReturn(Optional.of(existingRecurso));
        when(RecursoRepository.save(any(Recurso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RecursoDto result = recursoServices.atualizar(resourceId, recursoDto);

        // Assert
        assertNotNull(result);
        assertEquals(resourceId, result.getId());
        assertEquals(recursoDto.getChaveId(), result.getChaveId());
        assertEquals(recursoDto.getNome(), result.getNome());
        verify(RecursoRepository, times(1)).findById(resourceId);
        verify(RecursoRepository, times(1)).save(any(Recurso.class));

    }

    @Test
    void getAllResources_ReturnsEmptyList_WhenNoRecursosExist() {
        when(RecursoRepository.findAll()).thenReturn(Collections.emptyList());

        List<RecursoDto> result = recursoServices.buscarTodos();

        assertTrue(result.isEmpty());
        verify(RecursoRepository, times(1)).findAll();
    }


    @Test
    void atualizarCreationException_WhenErrorOccurs() {
        Long resourceId = 1L;
        RecursoDto recursoDto = new RecursoDto();
        Recurso existingRecurso = new Recurso();
        existingRecurso.setId(resourceId);

        when(RecursoRepository.findById(resourceId)).thenReturn(Optional.of(existingRecurso));
        when(RecursoRepository.save(any(Recurso.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(CriacaoException.class, () -> recursoServices.atualizar(resourceId, recursoDto));

        verify(RecursoRepository, times(1)).findById(resourceId);
        verify(RecursoRepository, times(1)).save(any(Recurso.class));

    }

    @Test
    void deleteDoesNotExist() {
        Long nonExistentResourceId = 999L;
        when(RecursoRepository.findById(nonExistentResourceId)).thenReturn(Optional.empty());

        NaoEncontradoException exception = assertThrows(NaoEncontradoException.class,
                () -> recursoServices.delete(nonExistentResourceId));

        String expectedMessage = String.format(Mensagem.INFO_NAO_ENCONTRADO, nonExistentResourceId);
        assertEquals(expectedMessage, exception.getMessage());
        verify(RecursoRepository, times(1)).findById(nonExistentResourceId);
        verify(RecursoRepository, never()).deleteById(anyLong());
    }

}