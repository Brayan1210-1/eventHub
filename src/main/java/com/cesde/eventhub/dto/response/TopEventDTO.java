package com.cesde.eventhub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopEventDTO {
    private String eventName;
    private long ticketsSold;
    private double revenue;
}