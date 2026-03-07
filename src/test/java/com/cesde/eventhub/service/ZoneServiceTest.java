package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cesde.eventhub.dto.request.ZoneRegisterDTO;
import com.cesde.eventhub.dto.response.ZoneResponseDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.entity.Zone;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.ZoneMapper;
import com.cesde.eventhub.repository.ZoneRepository;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private PlaceService placeService;

    @Spy
    private final ZoneMapper zoneMapper = Mappers.getMapper(ZoneMapper.class);

    @InjectMocks
    private ZoneService zoneService;

    private Place place;
    private Zone zoneEntity;
    private ZoneRegisterDTO zoneRegisterDTO;

    @BeforeEach
    void setUp() {
        
        place = new Place();
        place.setId(1L);
        place.setName("Estadio");
        place.setTotalCapacity(1000);
        place.setActive(true);

       
        zoneEntity = new Zone();
        zoneEntity.setId(10L);
        zoneEntity.setName("VIP");
        zoneEntity.setCapacity(200);
        zoneEntity.setPlace(place);

       
        zoneRegisterDTO = new ZoneRegisterDTO();
        zoneRegisterDTO.setName("VIP");
        zoneRegisterDTO.setCapacity(200);
        zoneRegisterDTO.setPlaceId(1L);
    }

    @Test
    void createZone_ShouldSaveSuccessfully() {
        
        when(placeService.validatePlaceIsActiveAndExists(1L)).thenReturn(place);
        when(zoneRepository.sumCapacityByPlaceId(1L)).thenReturn(100); // Ya hay 100 ocupados
        when(zoneRepository.save(any(Zone.class))).thenReturn(zoneEntity);

        ZoneResponseDTO result = zoneService.createZone(zoneRegisterDTO);

        assertNotNull(result);
        assertEquals("VIP", result.getName());
        verify(zoneRepository).save(any(Zone.class));
    }

    @Test
    void validateCapacity_ShouldThrowException_WhenCapacityExceeded() {
        when(zoneRepository.sumCapacityByPlaceId(1L)).thenReturn(900);

        assertThrows(InvalidRegistration.class, () -> {
            zoneService.validateCapacity(place, 200, 0);
        });
    }

    @Test
    void updateZone_ShouldUpdateSuccessfully() {
      
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zoneEntity));
        when(placeService.validatePlaceIsActiveAndExists(1L)).thenReturn(place);
        when(zoneRepository.sumCapacityByPlaceId(1L)).thenReturn(200);
        when(zoneRepository.save(any(Zone.class))).thenReturn(zoneEntity);

  
        ZoneResponseDTO result = zoneService.updateZone(10L, zoneRegisterDTO);

       
        assertNotNull(result);
        verify(zoneRepository).save(any(Zone.class));
    }

    @Test
    void findById_ShouldThrowDataNotFound_WhenNotExists() {
        when(zoneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DataNotFound.class, () -> zoneService.findById(99L));
    }

    @Test
    void getZonesByPlace_ShouldReturnPage() {
       
        Pageable pageable = PageRequest.of(0, 10);
        Page<Zone> page = new PageImpl<>(List.of(zoneEntity));
        when(zoneRepository.findByPlaceId(1L, pageable)).thenReturn(page);

        Page<ZoneResponseDTO> result = zoneService.getZonesByPlace(1L, pageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(placeService).findByPlaceId(1L);
    }

    @Test
    void deleteZone_ShouldInvokeDelete() {
     
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zoneEntity));

        zoneService.deleteZone(10L);

        verify(zoneRepository).deleteById(10L);
    }
}