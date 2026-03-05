package com.cesde.eventhub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDTO {
	
	@NotBlank(message = "El nombre es obligatorio")
	private String name;
	
	@NotBlank(message = "La direccion es obligatoria")
	private String address;
	
	@NotBlank(message = "La ciudad no puede ser nula")
	@Size(min = 5, message = "La ciudad debe tener mas de 5 caracteres")
	private String city;
	
	@NotNull(message = "La capacidad no puede ser nula")
	@Min(value = 1, message = "La capacidad total no puede ser menor a 1")
	private Integer totalCapacity;
	
	private String description;
	
	
	private String imageUrl;
	
}
