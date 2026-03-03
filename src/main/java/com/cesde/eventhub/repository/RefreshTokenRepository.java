package com.cesde.eventhub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.RefreshToken;
import com.cesde.eventhub.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByToken(String token);
	
	Optional<RefreshToken> findByUserId(UUID id);
	
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.user = :user ")
	void deleteByUsuario (@Param("user") User user);
	
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.expirationDate < CURRENT_TIMESTAMP")
	void deleteByexpirationDate();
}
