package com.cesde.eventhub.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventDetailPublicDTO;
import com.cesde.eventhub.dto.response.EventPublicDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.dto.response.ZoneDetailDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.TicketPrice;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

   
    Event toEntity(EventRegisterDTO dto);

    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "place.name", target = "placeName")
    @Mapping(source = "organizer.email", target = "organizerEmail")
    EventResponseDTO toDTO(Event entity);
    
    
    @Mapping(source = "place.name", target = "placeName")
    @Mapping(source = "place.city", target = "city")
    @Mapping(target = "minPrice", expression = "java(calculateMinPrice(event))")
    @Mapping(target = "maxPrice", expression = "java(calculateMaxPrice(event))")
    EventPublicDTO toPublicDTO(Event event);
   
    
    @Mapping(source = "place.name", target = "placeName")
    @Mapping(source = "place.city", target = "city")
    @Mapping(source = "place.address", target = "address")
    @Mapping(target = "salesOpen", expression = "java(isSalesOpen(event))")
    @Mapping(source = "ticketPrices", target = "zones")
    EventDetailPublicDTO toDetailDTO(Event event);
    
    @Mapping(source = "zone.name", target = "zoneName") 
    @Mapping(source = "zone.id", target = "id")
    ZoneDetailDTO toZoneDetailDTO(TicketPrice ticketPrice);
    
    default boolean isSalesOpen(Event event) {
         LocalDateTime now = LocalDateTime.now();
        if (event.getSalesStartDate() == null || event.getSalesEndDate() == null) {
            return false;
        }
        return now.isAfter(event.getSalesStartDate()) && now.isBefore(event.getSalesEndDate());
    }
    
    default double calculateMinPrice(Event event) {
        if (event.getTicketPrices() == null || event.getTicketPrices().isEmpty()) {
            return 0.0;
        }
        return event.getTicketPrices().stream()
                .mapToDouble(TicketPrice::getPrice)
                .min()
                .orElse(0.0);
    }

   
    default double calculateMaxPrice(Event event) {
        if (event.getTicketPrices() == null || event.getTicketPrices().isEmpty()) {
            return 0.0;
        }
        return event.getTicketPrices().stream()
                .mapToDouble(TicketPrice::getPrice)
                .max()
                .orElse(0.0);
    }
}