package br.com.controlapi.services;

import br.com.controlapi.constants.Messages;
import br.com.controlapi.dto.ResourceDto;
import br.com.controlapi.exception.ResourceNotFoundException;
import br.com.controlapi.exception.ResourceCreationException;
import br.com.controlapi.model.Resource;
import br.com.controlapi.repository.ResourceRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResourceServices {

    private static final Logger logger = LoggerFactory.getLogger(ResourceServices.class);
    private ResourceRepository resourceRepository;
    protected Logger getLogger() {
        return logger;
    }

    @Transactional
    public ResourceDto createResource(@Valid ResourceDto resourceDTO) {
        getLogger().info("Iniciando a criação do recurso com key: {}", resourceDTO.getKey());
        try {
            if (resourceRepository.existsByKey(resourceDTO.getKey())) {
                throw new ResourceCreationException(String.format(Messages.RESOURCE_ALREADY_EXISTS, resourceDTO.getKey()));
            }
            Resource resource = new Resource(resourceDTO);
            Resource savedResource = resourceRepository.save(resource);
            getLogger().info(String.format(Messages.RESOURCE_CREATION_SUCCESS, savedResource.getId()));
            return new ResourceDto(savedResource);
        } catch (Exception e) {
            getLogger().error(String.format(Messages.RESOURCE_CREATION_ERROR, e.getMessage()));
            throw new ResourceCreationException(String.format(Messages.RESOURCE_CREATION_ERROR, e.getMessage()));
        }
    }

    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(ResourceDto::new)
                .collect(Collectors.toList());
    }

    public ResourceDto getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId).map(ResourceDto::new)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.RESOURCE_NOT_FOUND, resourceId)));
    }

    public ResourceDto updateResource(Long resourceId, ResourceDto resourceDto) {
        return resourceRepository.findById(resourceId).map(resource -> {
            try {
                BeanUtils.copyProperties(resourceDto, resource, "id");
                getLogger().info(String.format(Messages.RESOURCE_UPDATE_SUCCESS, resource.getId()));
                return new ResourceDto(resourceRepository.save(resource));
            } catch (Exception e) {
                getLogger().error(String.format(Messages.RESOURCE_UPDATE_ERROR, e.getMessage()));
                throw new ResourceCreationException(String.format(Messages.RESOURCE_UPDATE_ERROR, e.getMessage()));
            }

        }).orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.RESOURCE_NOT_FOUND, resourceId)));
    }

    public String deleteResource(Long resourceId) {
        return resourceRepository.findById(resourceId).map(resource -> {
            resourceRepository.deleteById(resourceId);
            return Messages.RESOURCE_DELETION_SUCCESS;
        }).orElseThrow(() -> new ResourceNotFoundException(String.format(Messages.RESOURCE_NOT_FOUND, resourceId)));
    }
}
