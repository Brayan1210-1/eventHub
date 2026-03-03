package com.cesde.eventhub.entity;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.cesde.eventhub.enums.UserRoles;

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
public class User {

	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(name = "nombre", nullable = false, length = 50)
	private String name;

	@Column(name = "apellido", nullable = false, length = 50)
	private String lastName;

	@Column(name = "documento", nullable = false, length = 20, unique = true)
	private String document;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private RefreshToken refreshToken;
	
	@Email
	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;

	@Column(name = "telefono", length = 30, unique = true, nullable = false)
	private String phone;

	@Column(name = "contrasena", nullable = false)
	private String password;

	@Column(name = "rol", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoles roles;

	@Column(name = "activo", nullable = false)
	private Boolean active;

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
