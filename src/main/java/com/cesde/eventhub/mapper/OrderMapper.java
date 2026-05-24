package com.cesde.eventhub.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.response.OrderHistoryResponseDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.entity.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	
	
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.status", target = "status")
    @Mapping(source = "order.total", target = "totalAmount") 
    @Mapping(target = "expirationTime", source = "expirationTime")
    OrderResponseDTO toResponseDTO(Order order, LocalDateTime expirationTime);
    
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.event.name", target = "eventName")
    @Mapping(source = "order.createdAt", target = "purchaseDate")
    @Mapping(source = "order.client.name", target = "buyerName")
    @Mapping(source = "order.total", target = "totalAmount")
    @Mapping(source = "order.status", target = "status")
    @Mapping(target = "quantity", expression = "java(order.getTickets() != null ? order.getTickets().size() : 0)")
    OrderHistoryResponseDTO toHistoryDTO(Order order);

}