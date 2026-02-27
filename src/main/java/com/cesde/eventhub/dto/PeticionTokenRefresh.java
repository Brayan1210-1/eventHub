package com.cesde.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeticionTokenRefresh {

	@NotBlank(message = "el token es obligatorio")
	private String tokenRefresh;
}
