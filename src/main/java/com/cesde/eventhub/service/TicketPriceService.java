package com.cesde.eventhub.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.request.TicketPriceRegisterDTO;
import com.cesde.eventhub.dto.response.TicketPriceResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.TicketPrice;
import com.cesde.eventhub.entity.Zone;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.TicketPriceMapper;
import com.cesde.eventhub.repository.TicketPriceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketPriceService {

	private final ZoneService zoneService;
	private final EventService eventService;
	private final PlaceService placeService;
	private final UserService userService;
	private final TicketPriceMapper ticketMapper;
	private final TicketPriceRepository ticketPriceRepository;
	
	
	@Transactional
	@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
	public TicketPriceResponseDTO createTicketPrice(TicketPriceRegisterDTO ticket) {
	  
	    Event event = eventService.validateEventExists(ticket.getEventId());
	    
	    UUID onwerEventId = event.getOrganizer().getId();
	    
	    userService.validateAuthority(onwerEventId);
	    
	    Zone zone = zoneService.findById(ticket.getZoneId());
	    
	    placeService.validatePlaceIsActiveAndExists(zone.getPlace().getId()); 
	    
	   validateAvaliableQuantity(ticket, zone);

	    
	    TicketPrice ticketPrice = ticketMapper.toEntity(ticket);
	    ticketPrice.setEvent(event);
	    ticketPrice.setZone(zone);

	  
	    TicketPrice savedPrice = ticketPriceRepository.save(ticketPrice);
	    return ticketMapper.toDTO(savedPrice);
	}
	
	 @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
	public List<TicketPriceResponseDTO> getPricesByEvent(Long eventId) {
	  
	    eventService.validateEventExists(eventId);
	    
	    
	    return ticketPriceRepository.findAllByEventId(eventId)
	            .stream()
	            .map(ticketMapper::toDTO)
	            .collect(Collectors.toList());
	}
	
	public void validateAvaliableQuantity(TicketPriceRegisterDTO ticket, Zone zone ) {
		
		 if (ticket.getAvailableQuantity() > zone.getCapacity()) {
		        throw new InvalidRegistration("La cantidad de tickets no puede superar a la capacidad de la zona");
		    }
	}
}
