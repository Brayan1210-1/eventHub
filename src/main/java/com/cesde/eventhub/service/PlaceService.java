package com.cesde.eventhub.service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.mapper.PlaceMapper;
import com.cesde.eventhub.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	
	private final PlaceMapper mapper;
	
	private final PlaceRepository placeRepository;

	@Secured("ROLE_ADMIN")
	public PlaceDTO createPlace(PlaceDTO place) {
		
		//if(lugar.getCapacidad_total() == null || lugar.getCiudad() == null || lugar.getDireccion() == null
			//	|| lugar.getNombre() == null) {
			//throw new RuntimeException("Los campos capacidad, ciudad, direccion y nombre no pueden ser nulos");
		//}
		
		Place placeToSave = mapper.toEntity(place);
		placeToSave.setActive(true);
		Place placeSaved = placeRepository.save(placeToSave);
		
		return mapper.toDTO(placeSaved);
	}
	
	@Secured("ROLE_ADMIN")
	public Page<Place> activesPlaces(Pageable pageable){
	
		return placeRepository.findByActiveTrue(pageable);
	}
	
	@Secured("ROLE_ADMIN")
	public UpdatePlaceDTO updatePlace(Long id, UpdatePlaceDTO place) {
		
		Optional<Place> optionalPlace = placeRepository.findById(id);
		
		if(!optionalPlace.isPresent()) {
			throw new RuntimeException("No existe un lugar con el id: " + id);
		}
		
		//USAR MAPSTRUCT
		Place foundPlace = optionalPlace.get();
		
		foundPlace.setName(place.getName());
		foundPlace.setCity(place.getCity());
		foundPlace.setTotal_capacity(place.getTotal_capacity());
		foundPlace.setDescription(place.getDescription());
		foundPlace.setAddress(place.getAddress());
		foundPlace.setActive(place.getActive());
		foundPlace.setImageUrl(place.getImageUrl());
		
		placeRepository.save(foundPlace);
		
		return mapper.toDTOUpdate(foundPlace);
	}
	
	@Secured("ROLE_ADMIN")
	public void deletePlace(Long id) {
		
		Optional<Place> optionalPlace = placeRepository.findById(id);
		if(optionalPlace.isEmpty()) {
			throw new RuntimeException("No se pudo encontrar el lugar con id: "+ id);
		}
		
		Place deletedPlace = optionalPlace.get();	
		placeRepository.delete(deletedPlace);
	}
	
	
}
