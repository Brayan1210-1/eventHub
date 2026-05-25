package com.cesde.eventhub.service;

import java.time.LocalDate;  
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.EventCancelDTO;
import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.request.FilterEventsPublicsDTO;
import com.cesde.eventhub.dto.response.EventDetailPublicDTO;
import com.cesde.eventhub.dto.response.EventPublicDTO;
import com.cesde.eventhub.dto.response.EventReportResponseDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.ZoneReportDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.EventStatus;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.exception.custom.Unauthorized;
import com.cesde.eventhub.mapper.EventMapper;
import com.cesde.eventhub.projections.ZoneStatsProjection;
import com.cesde.eventhub.repository.EventRepository;
import com.cesde.eventhub.repository.OrderRepository;
import com.cesde.eventhub.repository.TicketRepository;
import com.cesde.eventhub.utils.PaginationUtils;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.entity.TicketPrice;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final PlaceService placeService;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final TicketRepository ticketRepository;
    
    
	@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
	@Transactional(readOnly = true)
    public PaginatedResponseDTO<EventResponseDTO> getAllEvents(Pageable pageable) {
      Page<Event> events = eventRepository.findAllWithActivePlace(pageable);
           
       return PaginationUtils.toPaginatedResponse(events, eventMapper::toDTO);
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
    
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<EventPublicDTO> getPublicEvents(FilterEventsPublicsDTO filters, Pageable pageable) {
       
     	LocalDate today = LocalDate.now();
        LocalDate start = filters.getStartingDate();
        LocalDate end = filters.getEndDate();

      
        if (start != null && start.isBefore(today)) {
            throw new InvalidRegistration("La fecha de inicio no puede ser anterior a hoy.");
        }

        if (start == null) {
           
            start = today;
        }

        if (end != null && end.isBefore(start)) {
          
            throw new InvalidRegistration("La fecha de fin no puede ser anterior a la de inicio.");
        }
    	
        Page<Event> eventsPage = eventRepository.filterEventsPublics(
            filters.getCategory(),
            filters.getCity(),
            start,
            end,
            pageable
        );

        return PaginationUtils.toPaginatedResponse(eventsPage, eventMapper::toPublicDTO);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    @Transactional(readOnly = true)
    public EventReportResponseDTO getEventReport(Long eventId) {
        
        Event event = findEventById(eventId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            UUID authenticatedUserId = UUID.fromString(auth.getName());
            if (event.getOrganizer() == null || !event.getOrganizer().getId().equals(authenticatedUserId)) {
                throw new Unauthorized("Acceso denegado: No tienes permisos sobre las estadísticas de este evento.");
            }
        }

        long totalSold = ticketRepository.countByOrder_EventIdAndOrder_Status(eventId, OrderStatus.PAGADA);
        Double rawRevenue = ticketRepository.sumRevenueByEventId(eventId);
        double totalRevenue = rawRevenue != null ? rawRevenue : 0.0;

        long totalRemaining = event.getTicketPrices().stream()
                .mapToLong(TicketPrice::getAvailableQuantity) 
                .sum();

        List<ZoneStatsProjection> dbZoneStats = ticketRepository.getZoneStatsByEventId(eventId);

        List<ZoneReportDTO> zoneReports = event.getTicketPrices().stream().map(tp -> {
            String zoneName = tp.getZone().getName();

            ZoneStatsProjection stats = dbZoneStats.stream()
                    .filter(s -> s.getZoneName().equals(zoneName))
                    .findFirst()
                    .orElse(null);

            long sold = stats != null ? stats.getTicketsSold() : 0L;
            double revenue = stats != null ? stats.getRevenue() : 0.0;
            
            long remaining = tp.getAvailableQuantity(); 

            return new ZoneReportDTO(zoneName, sold, remaining, revenue);
        }).toList();

        Long totalAttendees = null;
        if (event.getEventDate().isBefore(LocalDate.now())) {
            totalAttendees = ticketRepository.countByOrder_EventIdAndStatus(eventId, TicketStatus.USADA);
        }

        return eventMapper.toReportDTO(
                event, 
                totalSold, 
                totalRemaining, 
                totalRevenue, 
                totalAttendees, 
                zoneReports
        );
    }
    
    @Transactional(readOnly = true)
    public EventDetailPublicDTO getEventDetail(Long id) {
        Event event = eventRepository.findWithDetailsById(id)
                .orElseThrow(() -> new DataNotFound("El evento con ID " + id + " no fue encontrado."));
        return eventMapper.toDetailDTO(event);
    }
    
    public Event findEventById(Long id) {
    	
 	   Event event = eventRepository.findById(id)
 			   .orElseThrow(() -> new DataNotFound("No existe un evento con ese id"));
 	   
 	   return event;
 }
    
    
    
    
}