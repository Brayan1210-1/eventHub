package com.cesde.eventhub.dto.request;


import com.cesde.eventhub.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPay {

	@NotNull(message = "El método de pago es obligatorio")
	private PaymentMethod paymentMethod;
	
	@NotNull(message = "La referencia de pago es obligatoria")
	private String paymentReference;
}
