package com.cesde.eventhub.dto.response;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class OrderHistoryResponseDTO {
    private UUID orderId;
    private String eventName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate; 
    private String buyerName;
    private int quantity;
    private Double totalAmount;
    private String status;
}