package com.cesde.eventhub.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRegisterDTO {
	
	@NotBlank(message = "El nombre no puede ser nulo")
	private String name;
	
	@NotNull
    @Min(value = 1,message = " la capacidad debe ser de por lo menos 1")
	private int capacity;
	
	@NotBlank(message = "La decripcion es obligatoria")
	private String description;
	
	@NotNull(message = "El id del lugar es obligatorio")
	private Long placeId;

}
