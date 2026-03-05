package com.cesde.eventhub.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.dto.response.PlaceResponseDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.mapper.PlaceMapper;
import com.cesde.eventhub.repository.PlaceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	
	private final PlaceMapper placeMapper;
	
	private final PlaceRepository placeRepository;
	
	public Place validatePlaceExists(Long id) {
		return placeRepository.findById(id)
	            .orElseThrow(() -> new DataNotFound("No existe un lugar con ese id: " + id));
	}

	@Secured("ROLE_ADMIN")
	@Transactional
	public PlaceDTO createPlace(PlaceDTO place) {
		
		//if(lugar.getCapacidad_total() == null || lugar.getCiudad() == null || lugar.getDireccion() == null
			//	|| lugar.getNombre() == null) {
			//throw new RuntimeException("Los campos capacidad, ciudad, direccion y nombre no pueden ser nulos");
		//}
		
		Place placeToSave = placeMapper.toEntity(place);
		placeToSave.setActive(true);
		Place placeSaved = placeRepository.save(placeToSave);
		
		return placeMapper.toDTO(placeSaved);
	}
	
	@Secured("ROLE_ADMIN")
	public Page<PlaceResponseDTO> activesPlaces(Pageable pageable) {
	    return placeRepository.findByActiveTrue(pageable)
	            .map(placeMapper::toPage); 
	}
	
	@Secured("ROLE_ADMIN")
	@Transactional
	public UpdatePlaceDTO updatePlace(Long id, UpdatePlaceDTO placeDTO) {
		
		
		//USAR MAPSTRUCT
		Place foundPlace = validatePlaceExists(id);
		
		placeMapper.updateEntityFromDTO(placeDTO, foundPlace);
		
		placeRepository.save(foundPlace);
		
		return placeMapper.toDTOUpdate(foundPlace);
	}
	
	@Secured("ROLE_ADMIN")
	public void deletePlace(Long id) {
		
		Place deletedPlace = validatePlaceExists(id);
		placeRepository.delete(deletedPlace);
		
	}
	
	
	
}
