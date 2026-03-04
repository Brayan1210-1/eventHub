package com.cesde.eventhub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>{
	
	public Optional<Client> findByName(String name);
	
	public Optional<Client> findByUserId(String id);

	public boolean existsByDocument(String document);
	
	public boolean existsByPhone(String phone);
}
