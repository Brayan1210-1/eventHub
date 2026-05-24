package com.cesde.eventhub.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.request.TicketValidationRequestDTO;
import com.cesde.eventhub.dto.response.TicketValidationResponseDTO;
import com.cesde.eventhub.entity.Client;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Ticket;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {
	
	private final TicketRepository ticketRepository;
	

	@PreAuthorize("hasRole('ORGANIZADOR')")
    @Transactional
    public TicketValidationResponseDTO validateTicket(TicketValidationRequestDTO request) {
		
	
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID authenticatedOrganizerId = UUID.fromString(username);
            
        Optional<Ticket> optionalTicket = ticketRepository.findByCode(request.getTicketCode());
        if (optionalTicket.isEmpty()) {
            return TicketValidationResponseDTO.builder()
                    .isValid(false)
                    .message(" CÓDIGO NO ENCONTRADO: Esta boleta no existe en el sistema.")
                    .build();
        }
        
        Ticket ticket = optionalTicket.get();
        Event event = ticket.getOrder().getEvent(); 
        Client client = ticket.getOrder().getClient();

        if (event.getOrganizer() == null || !event.getOrganizer().getId().equals(authenticatedOrganizerId)) {
            return TicketValidationResponseDTO.builder()
                    .isValid(false)
                    .message(" NO AUTORIZADO: No tienes permisos para gestionar la entrada de este evento.")
                    .build();
        }
       
        if (!event.getId().equals(request.getEventId())) {
            return buildInvalidResponse("❌ EVENTO INCORRECTO: Esta boleta pertenece a: " + event.getName(), ticket, client);
        }

        LocalDate today = LocalDate.now();
        if (!event.getEventDate().equals(today)) {
            return buildInvalidResponse("❌ FECHA INCORRECTA: El evento está programado para el " + event.getEventDate(), ticket, client);
        }

 
        if (ticket.getStatus() == TicketStatus.USADA) {
            return buildInvalidResponse(" Esta boleta ya fue usada y escaneada.", ticket, client);
        }
        if (ticket.getStatus() != TicketStatus.ACTIVA) {
            return buildInvalidResponse("❌ BOLETA INACTIVA: El estado actual es " + ticket.getStatus(), ticket, client);
        }

       LocalDateTime used = LocalDateTime.now();
        
        ticket.setStatus(TicketStatus.USADA);
        ticket.setUsedDate(used);
        
        ticketRepository.save(ticket);

        return TicketValidationResponseDTO.builder()
                .isValid(true)
                .message("ACCESO PERMITIDO")
                .attendeeName(client.getName())
                .attendeeDocument(client.getDocument())
                .zoneName(ticket.getTicketPrice().getZone().getName())
                .build();
    }

    private TicketValidationResponseDTO buildInvalidResponse(String msg, Ticket ticket, Client client) {
        return TicketValidationResponseDTO.builder()
                .isValid(false)
                .message(msg)
                .attendeeName(client.getName())
                .attendeeDocument(client.getDocument())
                .zoneName(ticket.getTicketPrice().getZone().getName())
                .build();
    }
}
