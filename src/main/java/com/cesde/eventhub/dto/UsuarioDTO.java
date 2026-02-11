package com.cesde.eventhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellido;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String contrasena;

	@NotBlank
	private String documento;

	@NotBlank
	private String telefono;


}
