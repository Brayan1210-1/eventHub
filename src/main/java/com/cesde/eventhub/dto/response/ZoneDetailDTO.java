package com.cesde.eventhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDetailDTO {
	private Long id;
    private String zoneName;
    private Double price;
    private Integer availableQuantity;
}