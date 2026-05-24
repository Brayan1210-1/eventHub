package com.cesde.eventhub.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class TicketValidationResponseDTO {
    private boolean isValid;
    private String message;
    
    
    private String attendeeName;
    private String attendeeDocument;
    private String zoneName;
}