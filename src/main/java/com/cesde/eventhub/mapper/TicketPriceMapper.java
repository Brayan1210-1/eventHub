package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.TicketPriceRegisterDTO;
import com.cesde.eventhub.dto.response.TicketPriceResponseDTO;
import com.cesde.eventhub.entity.TicketPrice;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
	public interface TicketPriceMapper {

	    @Mapping(source = "event.id", target = "eventId")
	    @Mapping(source = "event.name", target = "eventName")
	    @Mapping(source = "zone.name", target = "zoneName")
	    @Mapping(source = "zone.capacity", target = "zoneMaxCapacity")
	    @Mapping(source = "event.organizer.email", target = "organizerEmail")
	    TicketPriceResponseDTO toDTO(TicketPrice ticketPrice);

	  
	    @Mapping(target = "id", ignore = true)
	    @Mapping(target = "event", ignore = true)
	    @Mapping(target = "zone", ignore = true)
	    TicketPrice toEntity(TicketPriceRegisterDTO dto);
	}

