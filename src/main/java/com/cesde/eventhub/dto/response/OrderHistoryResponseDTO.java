package com.cesde.eventhub.dto.response;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderHistoryResponseDTO {
    private UUID orderId;
    private String eventName;
    private LocalDateTime purchaseDate; 
    private String buyerName;
    private int quantity;
    private Double totalAmount;
    private String status;
}