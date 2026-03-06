package com.cesde.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EventCancelDTO {
    @NotBlank(message = "El motivo de cancelación es obligatorio")
    @Size(min = 10, message = "El motivo debe tener al menos 10 caracteres")
    private String reason;
}