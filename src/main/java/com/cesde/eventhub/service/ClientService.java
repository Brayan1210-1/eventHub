package com.cesde.eventhub.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cesde.eventhub.entity.Client;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService  {
	
	private final ClientRepository clientRepository;
	
	public Client findById(UUID id) {
		Client client = clientRepository.findById(id).
				orElseThrow(() -> new DataNotFound("Cliente no encontrado"));
		
		return client;
	}
	
	public Client findByUserId(UUID userId) {
		Client client = clientRepository.findByUserId(userId)
				.orElseThrow(() -> new DataNotFound("No se encontro un cliente con ese id"));
		
		return client;
	}

	
	public Client findByDocument(String document) {
		Client client = clientRepository.findByDocument(document)
				.orElseThrow(() -> new DataNotFound("no se encontro un cliente con ese documento"));
	
	return client;
	}
}