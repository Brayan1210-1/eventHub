package com.cesde.eventhub.dto.request;

import java.util.Set;

import com.cesde.eventhub.enums.UserRoles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUserRegisterDTO {

    @Email(message = "El formato del correo es inválido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotEmpty(message = "Debe especificar al menos un rol para el usuario")
    private Set<UserRoles> roles;
    
    @NotBlank(message = "El nombre no puede ser nulo o en blanco")
	private String name;

	@NotBlank(message = "El apellido es obligatorio")
	private String lastName;

	@NotBlank(message = "El documento es obligatorio")
	private String document;

	private String phone;
}