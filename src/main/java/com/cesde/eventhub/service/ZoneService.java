package com.cesde.eventhub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.request.ZoneRegisterDTO;
import com.cesde.eventhub.dto.response.ZoneResponseDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.entity.Zone;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.ZoneMapper;
import com.cesde.eventhub.repository.ZoneRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoneService {

	private final ZoneRepository zoneRepository;
	
	private final PlaceService placeService;
	
	private final ZoneMapper zoneMapper;
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public ZoneResponseDTO createZone(ZoneRegisterDTO zone) {
		
		Place place = placeService.validatePlaceIsActiveAndExists(zone.getPlaceId());
		
		validateCapacity(place, zone.getCapacity(), 0);
		
		Zone zoneToSave = zoneMapper.toEntity(zone);
		zoneToSave.setPlace(place);
		
		Zone zoneSave = zoneRepository.save(zoneToSave);
		
		return zoneMapper.toDTO(zoneSave);
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public ZoneResponseDTO updateZone(Long id, ZoneRegisterDTO updateZone) {
		
		Zone zoneFound = findById(id);
		Place place = placeService.validatePlaceIsActiveAndExists(updateZone.getPlaceId());
		
		validateCapacity(place, updateZone.getCapacity(), zoneFound.getCapacity());
		
		zoneMapper.updateEntityFromDTO(updateZone, zoneFound);
		zoneFound.setPlace(place);
		
		Zone savedZone = zoneRepository.save(zoneFound);
		
		return zoneMapper.toDTO(savedZone);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public Page<ZoneResponseDTO> getZonesByPlace(Long placeId, Pageable pageable) {
	  
	    placeService.validatePlaceExists(placeId);
	    
	    return zoneRepository.findByPlaceId(placeId, pageable)
	            .map(zoneMapper::toDTO);
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')") 
	public void deleteZone(Long id) {
	    
		findById(id);
	   
	    zoneRepository.deleteById(id);
	}
	
	public Zone findById(Long id) {
		return zoneRepository.findById(id)
				.orElseThrow(() -> new DataNotFound("No existe una zona con el id: " + id));
	}
	
	public void validateCapacity(Place place, int newCapacity, int currentZoneCapacity) {
		
		Integer sumCapacityZones = zoneRepository.sumCapacityByPlaceId(place.getId());
		
		int totalCapacityZones = (sumCapacityZones != null ? sumCapacityZones : 0);
		
		int sumTotalCapacityZones = totalCapacityZones - currentZoneCapacity + newCapacity;
		
		int totalPlace = place.getTotalCapacity();
		
		if(totalPlace < sumTotalCapacityZones) {
			throw new InvalidRegistration("La capacidad de los eventos no puede superar a la del lugar "
		+ "capacidad de todas las zonas: "+ sumTotalCapacityZones
		+ " Capacidad del lugar: " + totalPlace);
		}

		
	}
	
	
}
