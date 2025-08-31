package br.com.controlapi.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PaginacaoDTO<T>(
        List<T> content,
        int pageSize,
        int totalPages,
        long totalElements,
        int currentPage
) {
    public PaginacaoDTO(Page<T> page) {
        this(
                page.getContent(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber()
        );
    }
}