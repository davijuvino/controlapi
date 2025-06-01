package br.com.controlapi.services;

import br.com.controlapi.constants.Mensagem;
import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.exception.CriacaoException;
import br.com.controlapi.exception.NaoEncontradoException;
import br.com.controlapi.model.Recurso;
import br.com.controlapi.repository.RecursoRepository;
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
public class RecursoServices {

    private static final Logger logger = LoggerFactory.getLogger(RecursoServices.class);
    private RecursoRepository RecursoRepository;

    @Transactional
    public RecursoDto criarRecurso(@Valid RecursoDto recursoDTO) {
        logger.info("Iniciando a criação do recurso com key: {}", recursoDTO.getChaveId());
        try {
            if (RecursoRepository.existsByChaveId(recursoDTO.getChaveId())) {
                throw new CriacaoException(String.format(Mensagem.INFO_JA_EXISTE, recursoDTO.getChaveId()));
            }
            Recurso recurso = new Recurso(recursoDTO);
            Recurso savedRecurso = RecursoRepository.save(recurso);
            logger.info(Mensagem.CRIAR_OK, savedRecurso.getId());
            return new RecursoDto(savedRecurso);
        } catch (Exception e) {
            logger.error(Mensagem.CRIAR_NOK, e.getMessage());
            throw new CriacaoException(String.format(Mensagem.CRIAR_NOK, e.getMessage()));
        }
    }

    public List<RecursoDto> getAllRecursos() {
        return RecursoRepository.findAll().stream()
                .map(RecursoDto::new)
                .collect(Collectors.toList());
    }

    public RecursoDto getRecursoById(Long recursoId) {
        return RecursoRepository.findById(recursoId).map(RecursoDto::new)
                .orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, recursoId)));
    }

    public RecursoDto atualizarRecurso(Long recursoId, RecursoDto recursoDto) {
        return RecursoRepository.findById(recursoId).map(resource -> {
            try {
                BeanUtils.copyProperties(recursoDto, resource, "id");
                logger.info(Mensagem.ATUALIZAR_OK, resource.getId());
                return new RecursoDto(RecursoRepository.save(resource));
            } catch (Exception e) {
                logger.error(Mensagem.ATUALIZAR_NOK, e.getMessage());
                throw new CriacaoException(String.format(Mensagem.ATUALIZAR_NOK, e.getMessage()));
            }

        }).orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, recursoId)));
    }

    public String deletarRecurso(Long recursoId) {
        return RecursoRepository.findById(recursoId).map(resource -> {
            RecursoRepository.deleteById(recursoId);
            return Mensagem.DELETE_OK;
        }).orElseThrow(() -> new NaoEncontradoException(String.format(Mensagem.INFO_NAO_ENCONTRADO, recursoId)));
    }
}
