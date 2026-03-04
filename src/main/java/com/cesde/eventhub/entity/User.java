package com.cesde.eventhub.entity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

	@Email
	@Column(name = "email", nullable = false, length = 50, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;

	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(referencedColumnName = "id")
         )
    private Set<Role> roles = new HashSet<>();
	
	
	@Column(name = "activo", nullable = false)
	private Boolean active;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private RefreshToken refreshToken;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;
	
	
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
