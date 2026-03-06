package com.cesde.eventhub.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cesde.eventhub.enums.Category;
import com.cesde.eventhub.enums.EventStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime openingTime;
    private Category category;
    private String imageUrl;
    private EventStatus status;
    
   
    private String zoneName;
    private String placeName;
    private Long placeId;
    private String organizerEmail;
}