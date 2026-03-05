package com.cesde.eventhub.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceResponseDTO {

	private Long id;
    private String name;
    private String address;
    private String city;
    private Integer totalCapacity;
    private String imageUrl;
    private boolean active;
}
