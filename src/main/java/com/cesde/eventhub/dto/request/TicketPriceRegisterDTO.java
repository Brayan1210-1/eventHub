package com.cesde.eventhub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketPriceRegisterDTO {
    @NotNull(message = "El id del evento es requerido")
    private Long eventId;

    @NotNull(message = "El id de la zona es requerido")
    private Long zoneId;

    @Positive(message = "El precio debe ser positivo")
    private Double price;

    @Min(value = 1, message = "Available quantity must be at least 1")
    private Integer availableQuantity;
}