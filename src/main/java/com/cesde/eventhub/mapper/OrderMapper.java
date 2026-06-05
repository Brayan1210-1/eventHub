package com.cesde.eventhub.mapper;

import java.time.LocalDateTime;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import com.cesde.eventhub.dto.MyOrderDetailDTO;
import com.cesde.eventhub.dto.response.MyOrderDTO;
import com.cesde.eventhub.dto.response.MyTicketDTO;
import com.cesde.eventhub.dto.response.OrderHistoryResponseDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.entity.Ticket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
	
	@Mapping(source = "id", target = "ticketId")
    @Mapping(source = "ticketPrice.zone.name", target = "zoneName") // Sacamos la zona
    // Asumiendo que tu entidad Ticket tiene un campo String llamado "code"
    MyTicketDTO toMyTicketDTO(Ticket ticket);

    
	@Mapping(source = "id", target = "orderId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "event.eventDate", target = "eventDate")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "total", target = "totalAmount") // Mapea tu atributo 'total' double
    MyOrderDetailDTO toMyOrderDetailDTO(Order order);
	
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "event.eventDate", target = "eventDate")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "id", target = "orderId")
    MyOrderDTO toMyOrderDTO(Order order);
	
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
    
    
    
    @AfterMapping
    default void calculateDerivedFields(Order order, @MappingTarget MyOrderDetailDTO dto) {
        // 1. Seteamos la cantidad de boletas directo del tamaño de la lista
        if (order.getTickets() != null) {
            dto.setTicketQuantity(order.getTickets().size());
            
            // 2. Como la orden no tiene zona, la extraemos de forma segura del primer ticket
            if (!order.getTickets().isEmpty() && 
                order.getTickets().get(0).getTicketPrice() != null && 
                order.getTickets().get(0).getTicketPrice().getZone() != null) {
                
                dto.setZoneName(order.getTickets().get(0).getTicketPrice().getZone().getName());
            } else {
                dto.setZoneName("Zona Reservada");
            }
        } else {
            dto.setTicketQuantity(0);
            dto.setZoneName("Sin Zona");
        }
    }

}