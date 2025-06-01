package br.com.controlapi.services;

import br.com.controlapi.dto.RecursoDto;
import br.com.controlapi.exception.CriacaoException;
import br.com.controlapi.exception.JaExisteException;
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
    public RecursoDto criar(@Valid RecursoDto recursoDTO) {
        logger.info("Iniciando a criação do recurso com key: {}", recursoDTO.getChaveId());
        if (RecursoRepository.existsByChaveId(recursoDTO.getChaveId())) {
            throw new JaExisteException(recursoDTO.getChaveId());
        }
        try {
            Recurso recurso = new Recurso(recursoDTO);
            Recurso savedRecurso = RecursoRepository.save(recurso);
            logger.info("Criado com sucesso com ID: {}", savedRecurso.getId());
            return new RecursoDto(savedRecurso);
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar: {}", e.getMessage(), e);
            throw new CriacaoException(e.getMessage());
        }
    }

    public List<RecursoDto> buscarTodos() {
        return RecursoRepository.findAll()
                .stream()
                .map(RecursoDto::new)
                .collect(Collectors.toList());
    }

    public RecursoDto buscarPorId(Long recursoId) {
        return RecursoRepository.findById(recursoId)
                .map(RecursoDto::new)
                .orElseThrow(() -> new NaoEncontradoException(recursoId));
    }

    public RecursoDto atualizar(Long recursoId, RecursoDto recursoDto) {
        return RecursoRepository.findById(recursoId)
                .map(resource -> {
                    BeanUtils.copyProperties(recursoDto, resource, "id");
                    logger.info("Atualizado com sucesso com Id: {}", resource.getId());
                    return new RecursoDto(RecursoRepository.save(resource));
                })
                .orElseThrow(() -> new NaoEncontradoException(recursoId));
    }

    public String delete(Long recursoId) {
        return RecursoRepository.findById(recursoId)
                .map(resource -> {
                    RecursoRepository.deleteById(recursoId);
                    return "Excluído com sucesso!";
                })
                .orElseThrow(() -> new NaoEncontradoException(recursoId));
    }
}
