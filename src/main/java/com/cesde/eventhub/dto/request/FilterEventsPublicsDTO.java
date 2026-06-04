package com.cesde.eventhub.dto.request;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.cesde.eventhub.enums.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilterEventsPublicsDTO {
	
	private Category category;
	private String city;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startingDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate endDate;

}