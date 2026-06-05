package com.cesde.eventhub.dto.response;

import lombok.Data;
import java.util.UUID;

import com.cesde.eventhub.enums.TicketStatus;

@Data
public class MyTicketDTO {
    
    private Long ticketId; 
    
    private String zoneName;
    
    private UUID code; 
    
    private TicketStatus status;
}