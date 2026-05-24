package com.cesde.eventhub.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneReportDTO {
    private String zoneName;
    private long ticketsSold;
    private long ticketsRemaining;
    private double revenue;
}