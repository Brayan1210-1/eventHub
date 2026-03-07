package com.cesde.eventhub.dto.response;

import java.time.LocalDate;

import com.cesde.eventhub.enums.Category;

import lombok.Data;

@Data
public class EventPublicDTO {
    private Long id;
    private String name;
    private LocalDate eventDate;
    private String placeName;
    private String city;
    private String imageUrl;
    private double minPrice; 
    private double maxPrice; 
    private Category category;
}
