package com.cesde.eventhub.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlaceDTO {
		
		@NotBlank(message = "El nombre es obligatorio")
		private String name;
		
		@NotBlank(message = "La direccion es obligatoria")
		private String address;
		
		@NotBlank(message = "La ciudad no puede ser nula")
		@Size(min = 5, message = "La ciudad debe tener mas de 5 caracteres")
		private String city;
		
		@NotNull
		@Min(value = 1, message = "La capacidad debe ser positiva")
		private Integer total_capacity;
		
		private String description;
		
		private String imageUrl;
		
		@NotNull(message = "Debe haber un estado")
		private Boolean active;

}
