package com.cesde.eventhub.dto.response;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private PageMetaDTO meta;

    public static <T> PaginatedResponseDTO<T> create(List<T> content, org.springframework.data.domain.Page<?> page) {
        PageMetaDTO meta = PageMetaDTO.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
                
        return new PaginatedResponseDTO<>(content, meta);
    }


}