package com.cesde.eventhub.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class TicketValidationRequestDTO {
    @NotNull(message = "El código de la boleta es obligatorio")
    private UUID ticketCode;

    @NotNull(message = "El ID del evento actual es obligatorio")
    private Long eventId;
}