package com.cesde.eventhub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private Double totalAmount;
    private LocalDateTime expirationTime; 
}