package com.cesde.eventhub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

//ESTA SE USA PARA LOS ORGANIZADORES

@Data
@NoArgsConstructor
public class PlaceDetailDTO {
	private Long id;
    private String name;
    private String address;
    private String city;
    private Integer totalCapacityZones;
    private String description;
    private String imageUrl;
}
