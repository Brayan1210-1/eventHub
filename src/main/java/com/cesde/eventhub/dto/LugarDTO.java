package com.cesde.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LugarDTO {
	
	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;
	
	@NotBlank(message = "La direccion es obligatoria")
	private String direccion;
	
	@NotBlank(message = "La ciudad no puede ser nula")
	@Size(min = 5, message = "La ciudad debe tener mas de 5 caracteres")
	private String ciudad;
	
	@NotNull(message = "La capacidad no puede ser nula")
	private Integer capacidad_total;
	
	private String descripcion;
	
	
	private String imagenUrl;
	
}
