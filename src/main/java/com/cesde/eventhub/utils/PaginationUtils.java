package com.cesde.eventhub.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.cesde.eventhub.dto.response.PaginatedResponseDTO;

public class PaginationUtils {
	
	
    public static <E, D> PaginatedResponseDTO<D> toPaginatedResponse(Page<E> page, Function<E, D> mapperFunction) {
        List<D> convertedContent = page.getContent().stream()
                .map(mapperFunction)
                .collect(Collectors.toList());

        return PaginatedResponseDTO.create(convertedContent, page);
    }

   
    public static <D> PaginatedResponseDTO<D> toPaginatedResponse(Page<D> page) {
        return PaginatedResponseDTO.create(page.getContent(), page);
    }

}
