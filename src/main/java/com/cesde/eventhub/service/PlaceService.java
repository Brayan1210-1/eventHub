package com.cesde.eventhub.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.PlaceDetailDTO;
import com.cesde.eventhub.dto.PlaceListDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.PlaceResponseDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.PlaceMapper;
import com.cesde.eventhub.projections.PlaceBasicProjection;
import com.cesde.eventhub.repository.PlaceRepository;
import com.cesde.eventhub.utils.PaginationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	
	private final PlaceMapper placeMapper;
	
	private final PlaceRepository placeRepository;
	
	@Secured("ROLE_ADMIN")
	@Transactional
	public PlaceDTO createPlace(PlaceDTO place) {
		
		Place placeToSave = placeMapper.toEntity(place);
		placeToSave.setActive(true);
		Place placeSaved = placeRepository.save(placeToSave);
		
		return placeMapper.toDTO(placeSaved);
	}
	
	@Secured("ROLE_ADMIN")
	@Transactional(readOnly = true)
	public PaginatedResponseDTO<PlaceResponseDTO> activesPlaces(Pageable pageable) {
	    Page<Place> foundPages = placeRepository.findByActiveTrue(pageable); 
	    
	    return PaginationUtils.toPaginatedResponse(foundPages, placeMapper::toPage);
	}
	
	@Secured("ROLE_ADMIN")
	@Transactional
	public UpdatePlaceDTO updatePlace(Long id, UpdatePlaceDTO placeDTO) {
		
		
		//USAR MAPSTRUCT
		Place foundPlace = findByPlaceId(id);
		
		placeMapper.updateEntityFromDTO(placeDTO, foundPlace);
		
		placeRepository.save(foundPlace);
		
		return placeMapper.toDTOUpdate(foundPlace);
	}
	
	@Secured("ROLE_ADMIN")
	@Transactional
	public void deletePlace(Long id) {
		
		Place deletedPlace = findByPlaceId(id);
		placeRepository.delete(deletedPlace);
		
	}
	
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")
    public PaginatedResponseDTO<PlaceListDTO> getBasicPlacesList(String name, int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);

        Page<PlaceBasicProjection> projectionsPage = placeRepository.findActiveBasicPlacesWithFilters(name, pageable);

        return PaginationUtils.toPaginatedResponse(projectionsPage, placeMapper::toListDTO);
    }
	
	@Transactional(readOnly = true)
    public PlaceDetailDTO getPlaceDetails(Long id) {
        
        Place place = findByPlaceId(id);

        return placeMapper.toDetailDTO(place);
	}
	
	public Place findByPlaceId(Long id) {
		return placeRepository.findById(id)
	            .orElseThrow(() -> new DataNotFound("No existe un lugar con ese id: " + id));
	}
	
	public Place validatePlaceIsActiveAndExists(Long id) {
	    Place place = findByPlaceId(id);
	    if (!place.getActive()) {
	        throw new InvalidRegistration("El lugar '" + place.getName() + "' no está activo.");
	    }
	    return place;
	}

	
	
	
	
	
}
