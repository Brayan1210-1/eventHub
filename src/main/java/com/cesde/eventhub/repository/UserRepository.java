package com.cesde.eventhub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

	public Optional<User> findByNombre(String nombre);

	public Optional<User> findByEmail(String email);

	public boolean existsByEmail(String email);

	public boolean existsByDocumento(String documento);

}
