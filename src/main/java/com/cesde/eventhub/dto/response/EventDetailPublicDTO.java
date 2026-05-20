package com.cesde.eventhub.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailPublicDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private String imageUrl;
    private String placeName;
    private String city;
    private String address;
    private boolean salesOpen; 
    private List<ZoneDetailDTO> zones; 
}