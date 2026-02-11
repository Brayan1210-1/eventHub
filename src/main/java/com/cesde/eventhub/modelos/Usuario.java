package com.cesde.eventhub.modelos;
import java.time.LocalDateTime;

import com.cesde.eventhub.enumeraciones.RolesUsuario;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@Column(name = "apellido", nullable = false, length = 50)
	private String apellido;

	@Column(name = "documento", nullable = false, length = 20, unique = true)
	private String documento;

	@Email
	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;

	@Column(name = "telefono", length = 30, unique = true, nullable = false)
	private String telefono;

	@Column(name = "contrasena", nullable = false)
	private String contrasena;

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

	public Usuario(Long id, String nombre, String apellido, String documento, String email, String telefono,
			String contrasena, RolesUsuario rol, Boolean activo, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.documento = documento;
		this.email = email;
		this.telefono = telefono;
		this.contrasena = contrasena;
		this.rol = rol;
		this.activo = activo;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Usuario(Long id, String nombre, String email, String contraseña, RolesUsuario rol, Boolean activo,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.contrasena = contraseña;
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

}
