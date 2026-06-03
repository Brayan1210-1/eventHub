package com.cesde.eventhub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceListDTO {
	
	private Long id;
    private String name;
    private String city;
    private int totalCapacityZones;
    private int totalZones;
}
