package com.cesde.eventhub.dto.request;
import java.time.LocalDate;

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
	private LocalDate startingDate;
	private LocalDate endDate;

}