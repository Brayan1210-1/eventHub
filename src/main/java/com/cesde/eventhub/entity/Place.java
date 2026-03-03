package com.cesde.eventhub.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lugares")
public class Place{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombre", length = 40, nullable = false)
	private String nombre;
	
	@Column(name = "direccion", length = 60, nullable = false)
	private String direccion;
	
	@Column(name = "ciudad", length = 70, nullable = false)
	private String ciudad;
	
	@Column(name = "capacidad_total", nullable = false)
	private Integer capacidad_total;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "imagen_url")
	private String imagenUrl;
	
	@Column(name = "activo", nullable = false)
	private Boolean activo;
	
	@Column(name = "createdAt")
	private LocalDateTime createdAt;
	
	@Column(name = "updatedAt")
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
