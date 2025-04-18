package br.com.controlapi.services;

import br.com.controlapi.constants.Message;
import br.com.controlapi.dto.UserResourceDto;
import br.com.controlapi.exception.NotFoundException;
import br.com.controlapi.exception.UserResourceCreationException;
import br.com.controlapi.model.UserResource;
import br.com.controlapi.repository.UserResourceRepository;
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

public class UserResourceServicesTest {

    @Mock
    private UserResourceRepository UserResourceRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private UserResourceServices userResourceServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createResource_SuccessfullyCreatesResource() {
        UserResourceDto userResourceDto = new UserResourceDto();
        userResourceDto.setKeyId("testKey");
        UserResource userResource = new UserResource(userResourceDto);
        when(UserResourceRepository.existsByKeyId(userResourceDto.getKeyId())).thenReturn(false);
        when(UserResourceRepository.save(any(UserResource.class))).thenReturn(userResource);

        UserResourceDto result = userResourceServices.createResource(userResourceDto);

        assertEquals(userResourceDto.getKeyId(), result.getKeyId());
        verify(UserResourceRepository, times(1)).existsByKeyId(userResourceDto.getKeyId());
        verify(UserResourceRepository, times(1)).save(any(UserResource.class));
    }

    @Test
    void createResource_ThrowsException_WhenKeyExists() {
        UserResourceDto userResourceDto = new UserResourceDto();
        userResourceDto.setKeyId("testKey");
        when(UserResourceRepository.existsByKeyId(userResourceDto.getKeyId())).thenReturn(true);

        assertThrows(UserResourceCreationException.class, () -> userResourceServices.createResource(userResourceDto));
        verify(UserResourceRepository, times(1)).existsByKeyId(userResourceDto.getKeyId());
        verify(UserResourceRepository, never()).save(any(UserResource.class));
    }

    @Test
    void getAllResources_ReturnsListOfResources() {
        List<UserResource> userResources = Arrays.asList(new UserResource(), new UserResource());
        when(UserResourceRepository.findAll()).thenReturn(userResources);

        List<UserResourceDto> result = userResourceServices.getAllResources();

        assertEquals(userResources.size(), result.size());
        verify(UserResourceRepository, times(1)).findAll();
    }

    @Test
    void getResourceById_ReturnsResource() {
        UserResource userResource = new UserResource();
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.of(userResource));

        UserResourceDto result = userResourceServices.getResourceById(1L);

        assertNotNull(result);
        verify(UserResourceRepository, times(1)).findById(1L);
    }

    @Test
    void getResourceById_ThrowsException_WhenResourceNotFound() {
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userResourceServices.getResourceById(1L));
        verify(UserResourceRepository, times(1)).findById(1L);
    }

    @Test
    void updateResource_SuccessfullyUpdatesResource() {
        UserResourceDto userResourceDto = new UserResourceDto();
        UserResource userResource = new UserResource();
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.of(userResource));
        when(UserResourceRepository.save(any(UserResource.class))).thenReturn(userResource);

        UserResourceDto result = userResourceServices.updateResource(1L, userResourceDto);

        assertNotNull(result);
        verify(UserResourceRepository, times(1)).findById(1L);
        verify(UserResourceRepository, times(1)).save(any(UserResource.class));
    }

    @Test
    void updateResource_ThrowsException_WhenResourceNotFound() {
        UserResourceDto userResourceDto = new UserResourceDto();
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userResourceServices.updateResource(1L, userResourceDto));
        verify(UserResourceRepository, times(1)).findById(1L);
        verify(UserResourceRepository, never()).save(any(UserResource.class));
    }

    @Test
    void deleteResource_SuccessfullyDeletesResource() {
        UserResource userResource = new UserResource();
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.of(userResource));

        String result = userResourceServices.deleteResource(1L);

        assertEquals(Message.INFO_DELETION_SUCCESS, result);
        verify(UserResourceRepository, times(1)).findById(1L);
        verify(UserResourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResource_ThrowsException_WhenResourceNotFound() {
        when(UserResourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userResourceServices.deleteResource(1L));
        verify(UserResourceRepository, times(1)).findById(1L);
        verify(UserResourceRepository, never()).deleteById(1L);
    }

    @Test
    void updateResource_SuccessfullyUpdatesResource_WhenGivenValidInputAndExistingId() {
        // Arrange
        Long resourceId = 1L;
        UserResourceDto userResourceDto = new UserResourceDto();
        userResourceDto.setKeyId("updatedKey");
        userResourceDto.setName("updatedValue");


        UserResource existingUserResource = new UserResource();
        existingUserResource.setId(resourceId);
        existingUserResource.setKeyId("oldKey");
        existingUserResource.setName("oldValue");

        when(UserResourceRepository.findById(resourceId)).thenReturn(Optional.of(existingUserResource));
        when(UserResourceRepository.save(any(UserResource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserResourceDto result = userResourceServices.updateResource(resourceId, userResourceDto);

        // Assert
        assertNotNull(result);
        assertEquals(resourceId, result.getId());
        assertEquals(userResourceDto.getKeyId(), result.getKeyId());
        assertEquals(userResourceDto.getName(), result.getName());
        verify(UserResourceRepository, times(1)).findById(resourceId);
        verify(UserResourceRepository, times(1)).save(any(UserResource.class));

    }

    @Test
    void getAllResources_ReturnsEmptyList_WhenNoResourcesExist() {
        when(UserResourceRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResourceDto> result = userResourceServices.getAllResources();

        assertTrue(result.isEmpty());
        verify(UserResourceRepository, times(1)).findAll();
    }


    @Test
    void updateResource_ThrowsResourceCreationException_WhenErrorOccurs() {
        Long resourceId = 1L;
        UserResourceDto userResourceDto = new UserResourceDto();
        UserResource existingUserResource = new UserResource();
        existingUserResource.setId(resourceId);

        when(UserResourceRepository.findById(resourceId)).thenReturn(Optional.of(existingUserResource));
        when(UserResourceRepository.save(any(UserResource.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(UserResourceCreationException.class, () -> userResourceServices.updateResource(resourceId, userResourceDto));

        verify(UserResourceRepository, times(1)).findById(resourceId);
        verify(UserResourceRepository, times(1)).save(any(UserResource.class));

    }

    @Test
    void deleteResource_ThrowsResourceNotFoundException_WhenResourceDoesNotExist() {
        Long nonExistentResourceId = 999L;
        when(UserResourceRepository.findById(nonExistentResourceId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userResourceServices.deleteResource(nonExistentResourceId));

        String expectedMessage = String.format(Message.INFO_NOT_FOUND, nonExistentResourceId);
        assertEquals(expectedMessage, exception.getMessage());
        verify(UserResourceRepository, times(1)).findById(nonExistentResourceId);
        verify(UserResourceRepository, never()).deleteById(anyLong());
    }

}