package com.cesde.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

	@NotBlank(message = "El email es obligatorio")
	private String email;
	
	@NotBlank(message = "La contraseña no puede estar vacía")
	private String password;
}
