package com.cesde.eventhub.service;
/**
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.cesde.eventhub.dto.response.EventPublicDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.TicketPrice;
import com.cesde.eventhub.enums.Category;
import com.cesde.eventhub.mapper.EventMapper;
import com.cesde.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;

//USER STORY 010 in process
@Service
@RequiredArgsConstructor
public class ClientService {
	
	public EventMapper eventMapper;
	public EventRepository eventRepository;
	
	public Page<EventPublicDTO> getPublicEvents(Category category, String city, LocalDate start, LocalDate end, Pageable pageable) {
	    
	    Page<Event> events = eventRepository.findPublicEvents(category, city, start, end, pageable);

	    return events.map(event -> {
	        EventPublicDTO dto = eventMapper.toPublicDTO(event);
	        
	        
	        dto.setMinPrice(0.0);
	        dto.setMaxPrice(0.0);

	        
	        if (event.getTicketPrices() != null && !event.getTicketPrices().isEmpty()) {
	            Double min = event.getTicketPrices().get(0).getPrice();
	            Double max = event.getTicketPrices().get(0).getPrice();

	            for (TicketPrice tp : event.getTicketPrices()) {
	                if (tp.getPrice() != null) {
	                    if (tp.getPrice() < min) min = tp.getPrice();
	                    if (tp.getPrice() > max) max = tp.getPrice();
	                }
	            }
	            dto.setMinPrice(min);
	            dto.setMaxPrice(max);
	        }
	        
	        return dto;
	    });
	}
}
**/