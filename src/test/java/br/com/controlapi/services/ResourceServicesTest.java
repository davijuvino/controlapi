package br.com.controlapi.services;

import br.com.controlapi.constants.Messages;
import br.com.controlapi.dto.ResourceDto;
import br.com.controlapi.exception.ResourceNotFoundException;
import br.com.controlapi.exception.ResourceCreationException;
import br.com.controlapi.model.Resource;
import br.com.controlapi.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResourceServicesTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private ResourceServices resourceServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resourceServices = new ResourceServices(resourceRepository) {
            @Override
            protected Logger getLogger() {
                return logger;
            }
        };
    }

    @Test
    void createResource_SuccessfullyCreatesResource() {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setKey("testKey");
        Resource resource = new Resource(resourceDto);
        when(resourceRepository.existsByKey(resourceDto.getKey())).thenReturn(false);
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        ResourceDto result = resourceServices.createResource(resourceDto);

        assertEquals(resourceDto.getKey(), result.getKey());
        verify(resourceRepository, times(1)).existsByKey(resourceDto.getKey());
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void createResource_ThrowsException_WhenKeyExists() {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setKey("testKey");
        when(resourceRepository.existsByKey(resourceDto.getKey())).thenReturn(true);

        assertThrows(ResourceCreationException.class, () -> resourceServices.createResource(resourceDto));
        verify(resourceRepository, times(1)).existsByKey(resourceDto.getKey());
        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    void getAllResources_ReturnsListOfResources() {
        List<Resource> resources = Arrays.asList(new Resource(), new Resource());
        when(resourceRepository.findAll()).thenReturn(resources);

        List<ResourceDto> result = resourceServices.getAllResources();

        assertEquals(resources.size(), result.size());
        verify(resourceRepository, times(1)).findAll();
    }

    @Test
    void getResourceById_ReturnsResource() {
        Resource resource = new Resource();
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));

        ResourceDto result = resourceServices.getResourceById(1L);

        assertNotNull(result);
        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    void getResourceById_ThrowsException_WhenResourceNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceServices.getResourceById(1L));
        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    void updateResource_SuccessfullyUpdatesResource() {
        ResourceDto resourceDto = new ResourceDto();
        Resource resource = new Resource();
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        ResourceDto result = resourceServices.updateResource(1L, resourceDto);

        assertNotNull(result);
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void updateResource_ThrowsException_WhenResourceNotFound() {
        ResourceDto resourceDto = new ResourceDto();
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceServices.updateResource(1L, resourceDto));
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    void deleteResource_SuccessfullyDeletesResource() {
        Resource resource = new Resource();
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));

        String result = resourceServices.deleteResource(1L);

        assertEquals(Messages.RESOURCE_DELETION_SUCCESS, result);
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResource_ThrowsException_WhenResourceNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceServices.deleteResource(1L));
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, never()).deleteById(1L);
    }
}