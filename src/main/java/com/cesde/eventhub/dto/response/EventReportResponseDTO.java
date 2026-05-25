package com.cesde.eventhub.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EventReportResponseDTO {
    private Long eventId;
    private String eventName;
    private long totalTicketsSold;
    private long totalTicketsRemaining;
    private double totalRevenue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalAttendees; 
    
    private List<ZoneReportDTO> zoneReports;
}