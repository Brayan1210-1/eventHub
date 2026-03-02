package com.cesde.eventhub.modelos;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.cesde.eventhub.enumeraciones.RolesUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@Column(name = "apellido", nullable = false, length = 50)
	private String apellido;

	@Column(name = "documento", nullable = false, length = 20, unique = true)
	private String documento;

	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private RefreshToken refreshToken;
	
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
