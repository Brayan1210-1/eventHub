package com.cesde.eventhub.dto;

import com.cesde.eventhub.modelos.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	
	
	public UsuarioDTO() {
		
	}
	
	
	public UsuarioDTO( String nombre,  String apellido,   String email,
			 String contrasena,  String documento,  String telefono) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.contrasena = contrasena;
		this.documento = documento;
		this.telefono = telefono;
	}

	public UsuarioDTO(String nombre, String email) {
		this.nombre = nombre;
		this.email = email;
	}
	
	public Usuario haciaEntidad() {
		Usuario usuario = new Usuario();
		usuario.setNombre(this.nombre);
		usuario.setApellido(this.apellido);
		usuario.setEmail(this.email);
		usuario.setContrasena(this.contrasena);
		usuario.setDocumento(this.documento);
		usuario.setTelefono(this.telefono);
		
		return usuario;
	}
	
	public static UsuarioDTO desdeEntidadCliente(Usuario usuario) {
		return new UsuarioDTO (
		  usuario.getNombre(),
		  usuario.getEmail()
		  );
	}

}
