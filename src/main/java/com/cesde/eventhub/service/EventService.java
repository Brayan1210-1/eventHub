package com.cesde.eventhub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.EventCancelDTO;
import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.EventStatus;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.EventMapper;
import com.cesde.eventhub.repository.EventRepository;
import com.cesde.eventhub.repository.OrderRepository;
import com.cesde.eventhub.entity.Place;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final PlaceService placeService;
    private final UserService userService;
    private final EventMapper eventMapper;
    


    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public EventResponseDTO createEvent(EventRegisterDTO dto) {
        
        Place place = placeService.validatePlaceIsActiveAndExists(dto.getPlaceId());
        
         
       
        boolean isOccupied = eventRepository.existsByPlaceIdAndEventDateAndStatusNot(
            place.getId(), 
            dto.getEventDate(), 
            EventStatus.CANCELADO
        );
     
         String organizerIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
          UUID organizerId = UUID.fromString(organizerIdStr);
         
          User organizer = userService.findById(organizerId);
          
        if (isOccupied) {
            throw new InvalidRegistration("La zona " + place.getName() + " ya tiene un evento para esa fecha.");
        }

        Event event = eventMapper.toEntity(dto);
        event.setPlace(place);
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.BORRADOR); 
        
        return eventMapper.toDTO(eventRepository.save(event));
    }
    
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public void publishEvent(Long eventId) {
      
        Event event = findEventById(eventId);
       
        userService.validateAuthority(event.getOrganizer().getId());

       
        if (event.getStatus() != EventStatus.BORRADOR) {
            throw new InvalidRegistration("El evento no está en borrador.");
        }

        if (event.getTicketPrices() == null || event.getTicketPrices().isEmpty()) {
            throw new InvalidRegistration("No se puede publicar un evento sin precios para tickets");
        }

        if (event.getSalesStartDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRegistration("La fecha de inicio de ventas debe ser futura.");
        }

        event.setStatus(EventStatus.PUBLICADO);
        
        eventRepository.save(event);
    }
    
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public void cancelEvent(Long eventId, EventCancelDTO dto) {
        Event event = findEventById(eventId);
        
        userService.validateAuthority(event.getOrganizer().getId());

       
        if (event.getStatus() != EventStatus.BORRADOR && event.getStatus() != EventStatus.PUBLICADO) {
            throw new InvalidRegistration("No se puede cancelar un evento en estado " + event.getStatus());
        }

      
        List<Order> orders = orderRepository.findByEventId(event.getId());
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.PAGADA) {
                order.setStatus(OrderStatus.REEMBOLSADA);
                
                order.getTickets().forEach(t -> t.setStatus(TicketStatus.CANCELADA));
            } else if (order.getStatus() == OrderStatus.PENDIENTE) {
                order.setStatus(OrderStatus.CANCELADA);
            }
        }

       
        event.setStatus(EventStatus.CANCELADO);
        event.setCancellationReason(dto.getReason());
        eventRepository.save(event);

        System.out.println(" Evento cancelado y compradores notificados por email.");
    }
    
    public Event findEventById(Long id) {
    	
 	   Event event = eventRepository.findById(id)
 			   .orElseThrow(() -> new DataNotFound("No existe un evento con ese id"));
 	   
 	   return event;
 }
    
    
    
    
}