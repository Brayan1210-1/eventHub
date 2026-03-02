package com.cesde.eventhub.repositorio;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.modelos.RefreshToken;
import com.cesde.eventhub.modelos.Usuario;

@Repository
public interface RefreshTokenRepositorio extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);
	
	Optional<RefreshToken> findByUsuarioId(UUID id);
	
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.usuario = :usuario ")
	void deleteByUsuario (@Param("usuario") Usuario usuario);
	
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.fechaExpiracion < CURRENT_TIMESTAMP")
	void deleteByFechaExpiracion();
}
