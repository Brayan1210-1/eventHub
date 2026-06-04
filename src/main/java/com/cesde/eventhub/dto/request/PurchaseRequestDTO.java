package com.cesde.eventhub.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchaseRequestDTO {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debes comprar al menos 1 boleta")
    @Max(value = 4, message = "Máximo puedes comprar 4 boletas por transacción") 
    private Integer quantity;
    
}