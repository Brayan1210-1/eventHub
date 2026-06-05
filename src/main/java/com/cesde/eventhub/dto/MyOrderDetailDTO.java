package com.cesde.eventhub.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.cesde.eventhub.dto.response.MyTicketDTO;
import com.cesde.eventhub.enums.OrderStatus;

import lombok.Data;

@Data
public class MyOrderDetailDTO {
	 private UUID orderId;
	    private String eventName;
	    private LocalDate eventDate; 
	    private OrderStatus orderStatus;
	    private Double totalAmount;
	    private int ticketQuantity;
	    private String zoneName;
	    private List<MyTicketDTO> tickets;

}
