package br.com.controlapi.services;

import br.com.controlapi.constants.Message;
import br.com.controlapi.dto.UserResourceDto;
import br.com.controlapi.exception.NotFoundException;
import br.com.controlapi.exception.UserResourceCreationException;
import br.com.controlapi.model.UserResource;
import br.com.controlapi.repository.UserResourceRepository;
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
public class UserResourceServices {

    private static final Logger logger = LoggerFactory.getLogger(UserResourceServices.class);
    private UserResourceRepository UserResourceRepository;

    @Transactional
    public UserResourceDto createResource(@Valid UserResourceDto userResourceDTO) {
        logger.info("Iniciando a criação do recurso com key: {}", userResourceDTO.getKeyId());
        try {
            if (UserResourceRepository.existsByKeyId(userResourceDTO.getKeyId())) {
                throw new UserResourceCreationException(String.format(Message.INFO_ALREADY_EXISTS, userResourceDTO.getKeyId()));
            }
            UserResource userResource = new UserResource(userResourceDTO);
            UserResource savedUserResource = UserResourceRepository.save(userResource);
            logger.info(Message.INFO_CREATION_SUCCESS, savedUserResource.getId());
            return new UserResourceDto(savedUserResource);
        } catch (Exception e) {
            logger.error(Message.INFO_CREATION_ERROR, e.getMessage());
            throw new UserResourceCreationException(String.format(Message.INFO_CREATION_ERROR, e.getMessage()));
        }
    }

    public List<UserResourceDto> getAllResources() {
        return UserResourceRepository.findAll().stream()
                .map(UserResourceDto::new)
                .collect(Collectors.toList());
    }

    public UserResourceDto getResourceById(Long resourceId) {
        return UserResourceRepository.findById(resourceId).map(UserResourceDto::new)
                .orElseThrow(() -> new NotFoundException(String.format(Message.INFO_NOT_FOUND, resourceId)));
    }

    public UserResourceDto updateResource(Long resourceId, UserResourceDto userResourceDto) {
        return UserResourceRepository.findById(resourceId).map(resource -> {
            try {
                BeanUtils.copyProperties(userResourceDto, resource, "id");
                logger.info(Message.INFO_UPDATE_SUCCESS, resource.getId());
                return new UserResourceDto(UserResourceRepository.save(resource));
            } catch (Exception e) {
                logger.error(Message.INFO_UPDATE_ERROR, e.getMessage());
                throw new UserResourceCreationException(String.format(Message.INFO_UPDATE_ERROR, e.getMessage()));
            }

        }).orElseThrow(() -> new NotFoundException(String.format(Message.INFO_NOT_FOUND, resourceId)));
    }

    public String deleteResource(Long resourceId) {
        return UserResourceRepository.findById(resourceId).map(resource -> {
            UserResourceRepository.deleteById(resourceId);
            return Message.INFO_DELETION_SUCCESS;
        }).orElseThrow(() -> new NotFoundException(String.format(Message.INFO_NOT_FOUND, resourceId)));
    }
}
