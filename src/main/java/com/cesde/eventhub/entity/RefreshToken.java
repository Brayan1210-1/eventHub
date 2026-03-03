package com.cesde.eventhub.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "token", nullable = false, unique = true)
	private String token;
	
	@Column(name = "fecha_expiracion", nullable = false)
	private Instant expirationDate;
	
	public boolean isExpired() {
		return Instant.now().isAfter(this.expirationDate);
	}
}
