package com.cesde.eventhub.modelos;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import com.cesde.eventhub.enumeraciones.RolesUsuario;

@Entity
@Table(name = "usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;
	
	@Email
	@Column(name = "email", nullable = false, length = 50)
	private String email;
	
	@Column(name = "contraseña", nullable = false, length = 45)
	private String contraseña;
	
	@Column(name = "rol", nullable = false)
	@Enumerated(EnumType.STRING)
	private RolesUsuario rol;
	
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	
	@Column(name = "createdAt", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updatedAt", nullable = false)
	private LocalDateTime updatedAt;

	//Mega super increibles constructores
	public Usuario() {
		
	}
	
	public Usuario(Long id, String nombre, String email, String contraseña, RolesUsuario rol, Boolean activo,
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

	@PrePersist
	  protected void onCreate() {
	    this.createdAt = LocalDateTime.now();
	    this.updatedAt = LocalDateTime.now();
	  }
	
	@PreUpdate
	  protected void onUpdate() {
	    this.updatedAt = LocalDateTime.now();
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

	public RolesUsuario getRol() {
		return rol;
	}


	public void setRol(RolesUsuario rol) {
		this.rol = rol;
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
