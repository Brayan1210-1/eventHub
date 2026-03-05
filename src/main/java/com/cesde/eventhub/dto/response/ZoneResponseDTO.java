package com.cesde.eventhub.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponseDTO {

	
	private Long id;
	private String name;
	private int capacity;
	private String description;
	private Long place;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
