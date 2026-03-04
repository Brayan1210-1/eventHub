package com.cesde.eventhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

	@NotBlank(message = "El nombre no puede ser nulo o en blanco")
	private String name;

	@NotBlank(message = "El apellido es obligatorio")
	private String lastName;

	@Email(message = "Debe ser un email válido")
	@NotBlank(message = "El email es obligatorio")
	private String email;

	@NotBlank(message = "Debe ingresar una contraseña")
	private String password;

	@NotBlank(message = "El documento es obligatorio")
	private String document;

	
	private String phone;


}
