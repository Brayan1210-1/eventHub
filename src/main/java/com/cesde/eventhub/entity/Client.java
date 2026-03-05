package com.cesde.eventhub.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "nombre", nullable = false, length = 50)
    private String name;

    @Column(name = "apellido", nullable = false, length = 50)
    private String lastName;

    @Column(name = "documento", nullable = false, length = 20, unique = true)
    private String document;

    @Column(name = "telefono", length = 30, unique = true)
    private String phone;
    
    @Column(name = "created_At", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_At", nullable = false)
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
