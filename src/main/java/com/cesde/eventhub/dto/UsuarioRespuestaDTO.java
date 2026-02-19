package com.cesde.eventhub.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRespuestaDTO {
	
	private Long id;
	private String nombre;
	private String rol;
    private String apellido;
    private String email;
    private String documento;
    private String telefono;
}
