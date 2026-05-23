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

	@NotNull
	private Long order_id;
	
	@NotNull
	private PaymentMethod paymentMethod;
	
	@NotNull
	private String paymentReference;
}
