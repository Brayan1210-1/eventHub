package com.cesde.eventhub.dto.response;


import lombok.Data; 

import java.time.LocalDate;
import java.util.UUID;

import com.cesde.eventhub.enums.OrderStatus;

@Data
public class MyOrderDTO {
    private UUID orderId;
    private String eventName;
    private LocalDate eventDate; 
    private OrderStatus orderStatus;
}