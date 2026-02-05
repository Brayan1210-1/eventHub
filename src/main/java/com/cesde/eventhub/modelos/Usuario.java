package com.cesde.eventhub.modelos;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "email", nullable = false, length = 50)
	private String email;
	
	@Column(name = "contraseña", nullable = false, length = 45)
	private String contraseña;
	
	//El tipo de dato se cambiará a ENUM para solo tener una lista de roles
	//NO OLVIDAR
	@Column(name = "rol", nullable = false)
	private String rol;
	
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	
	@Column(name = "createdAt", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updatedAt", nullable = false)
	private LocalDateTime updatedAt;

	//Súper constructores
	public Usuario() {
		
	}
	
	
	public Usuario(Long id, String nombre, String email, String contraseña, String rol, Boolean activo,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.contraseña = contraseña;
		this.rol = rol;
		this.activo = activo;
		this.createdAt =  LocalDateTime.now();
		this.updatedAt =  LocalDateTime.now();
	}



	//Getters y setters
	public Long getId() {
		return id;
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

	public String getRol() {
		return rol;
	}

	public Boolean getActivo() {
		return activo;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
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


	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
