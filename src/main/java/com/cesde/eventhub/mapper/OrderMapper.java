package com.cesde.eventhub.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.entity.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

	
	
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.status", target = "status")
    @Mapping(source = "order.total", target = "totalAmount") 
    @Mapping(target = "expirationTime", source = "expirationTime")
    OrderResponseDTO toResponseDTO(Order order, LocalDateTime expirationTime);
    

}