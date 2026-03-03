package com.cesde.eventhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTokenRefresh {

	@NotBlank(message = "el token es obligatorio")
	private String tokenRefresh;
}
