package com.cesde.eventhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketPriceResponseDTO {
    private Long id;
    private Long eventId;
    private String eventName;
    private String zoneName;
    private String organizerEmail;
    private Double price;
    private Integer availableQuantity;
    private Integer zoneMaxCapacity;
}
