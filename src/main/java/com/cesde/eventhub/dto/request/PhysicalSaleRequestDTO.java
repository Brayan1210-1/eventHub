package com.cesde.eventhub.dto.request;

import com.cesde.eventhub.enums.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalSaleRequestDTO {

    
    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventId;

    @NotNull(message = "El ID de la zona es obligatorio")
    private Long zoneId;

    @Min(value = 1, message = "Debe vender al menos 1 boleta")
    private int quantity;

    @NotBlank(message = "El nombre del comprador es obligatorio")
    private String buyerName;

    @NotBlank(message = "El documento es obligatorio")
    private String buyerDocument;

    //@Email(message = "Email inválido")
    //@NotBlank(message = "El email es obligatorio")
    //private String buyerEmail;

    @NotBlank(message = "el apellido es obligatorio")
    private String buyerLastName;
    
    @NotBlank(message = "El teléfono es obligatorio")
    private String buyerPhone;

    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod paymentMethod;
}