package com.cesde.eventhub.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.entity.Zone;
import com.cesde.eventhub.enums.EventStatus;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.EventMapper;
import com.cesde.eventhub.repository.EventRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ZoneService zoneService;
    private final EventMapper eventMapper;
    private final UserService userService;
    

    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public EventResponseDTO createEvent(EventRegisterDTO dto) {
        
        
        Zone zone = zoneService.findById(dto.getZoneId());
          zoneService.validateActivePlace(zone);

       
        boolean isOccupied = eventRepository.existsByZoneIdAndEventDateAndStatusNot(
            zone.getId(), 
            dto.getEventDate(), 
            EventStatus.CANCELADO
        );
      
        
        
         String organizerIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
          UUID organizerId = UUID.fromString(organizerIdStr);


          User organizer = userService.findById(organizerId);

        if (isOccupied) {
            throw new InvalidRegistration("La zona " + zone.getName() + " ya tiene un evento para esa fecha.");
        }

        Event event = eventMapper.toEntity(dto);
        event.setZone(zone);
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.BORRADOR); 
        
        
        return eventMapper.toDTO(eventRepository.save(event));
    }
}