package com.cesde.eventhub.dto;

import com.cesde.eventhub.modelos.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioDTO {


	
	@NotBlank
	private String nombre;
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String contraseña;
	
	
	public UsuarioDTO() {
		
	}
	
	public UsuarioDTO(String nombre, String email, String contraseña) {
		this.nombre = nombre;
		this.email = email;
		this.contraseña = contraseña;
	}
	
	public UsuarioDTO(String nombre, String email) {
		this.nombre = nombre;
		this.email = email;
	}
	
	public Usuario haciaEntidad() {
		Usuario usuario = new Usuario();
		usuario.setNombre(this.nombre);
		usuario.setEmail(this.email);
		usuario.setContraseña(this.contraseña);
		return usuario;
	}
	
	public static UsuarioDTO desdeEntidad(Usuario usuario) {
		return new UsuarioDTO (
		  usuario.getNombre(),
		  usuario.getEmail()
		  );
	}
	

	public String getNombre() {
		return nombre;
	}

	public String getEmail() {
		return email;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	
}
