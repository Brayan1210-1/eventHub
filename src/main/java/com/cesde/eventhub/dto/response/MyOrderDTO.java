package com.cesde.eventhub.dto.response;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class MyOrderDTO {
    private UUID orderId;
    private String eventName;
    private LocalDate eventDate; 
    private String orderStatus;
    private List<MyTicketDTO> tickets;
}