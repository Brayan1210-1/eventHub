package com.cesde.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.dto.response.PlaceResponseDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.mapper.PlaceMapper;
import com.cesde.eventhub.repository.PlaceRepository;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {

     @Spy
	private final PlaceMapper placeMapper = Mappers.getMapper(PlaceMapper.class);
	
     @Mock
	private  PlaceRepository placeRepository;
     
     @InjectMocks
     private PlaceService placeService;
     
     private Place placeEntity;
     private PlaceDTO placeDTO;
     private UpdatePlaceDTO updateDTO;

     @BeforeEach
     void setUp() {
       
         placeEntity = new Place();
         placeEntity.setId(1L);
         placeEntity.setName("Centro de Convenciones");
         placeEntity.setAddress("Calle 123");
         placeEntity.setActive(true);

       
         placeDTO = new PlaceDTO();
         placeDTO.setName("Centro de Convenciones");
         placeDTO.setAddress("Calle 123");
      
         updateDTO = new UpdatePlaceDTO();
         updateDTO.setName("Nombre Actualizado");
     }

     @Test
     void findByPlaceId_ShouldThrowDataNotFound_WhenIdDoesNotExist() {
        
         Long idInexistente = 99L;
         when(placeRepository.findById(idInexistente)).thenReturn(Optional.empty());
         DataNotFound exception = assertThrows(DataNotFound.class, () -> {
             placeService.findByPlaceId(idInexistente);
         });

       
         assertTrue(exception.getMessage().contains("No existe un lugar con ese id: " + idInexistente));
     }
     
     @Test
     void createPlace_ShouldReturnSavedPlaceDTO() {
  
         when(placeRepository.save(any(Place.class))).thenReturn(placeEntity);

         PlaceDTO result = placeService.createPlace(placeDTO);

    
         assertNotNull(result);
         assertEquals("Centro de Convenciones", result.getName());
         verify(placeRepository).save(any(Place.class));
     }
     
     @Test
     void validatePlaceIsActiveAndExists_ShouldReturnPlace_WhenIsActive() {
      
         when(placeRepository.findById(1L)).thenReturn(Optional.of(placeEntity));
      
         Place result = placeService.validatePlaceIsActiveAndExists(1L);

         assertNotNull(result);
         assertTrue(result.getActive());
         assertEquals("Centro de Convenciones", result.getName());
     }
     
     @Test
     void validatePlaceIsActiveAndExists_ShouldThrowInvalidRegistration_WhenIsInactive() {
        
         placeEntity.setActive(false);
         when(placeRepository.findById(1L)).thenReturn(Optional.of(placeEntity));
     
         InvalidRegistration exception = assertThrows(InvalidRegistration.class, () -> {
             placeService.validatePlaceIsActiveAndExists(1L);
         });
       
         assertTrue(exception.getMessage().contains("no está activo"));
     }

     @Test
     void updatePlace_ShouldModifyAndReturnUpdatedDTO() {
        
         when(placeRepository.findById(1L)).thenReturn(Optional.of(placeEntity));
         when(placeRepository.save(any(Place.class))).thenReturn(placeEntity);
        
        UpdatePlaceDTO result = placeService.updatePlace(1L, updateDTO);
       
         assertNotNull(result);
       
         assertEquals("Nombre Actualizado", placeEntity.getName());
         verify(placeRepository).save(placeEntity);
     }

     @Test
     void deletePlace_ShouldInvokeDelete() {
       
         when(placeRepository.findById(1L)).thenReturn(Optional.of(placeEntity));
       
         placeService.deletePlace(1L);

         verify(placeRepository).delete(placeEntity);
     }

     @Test
     void activesPlaces_ShouldReturnPageOfResponseDTOs() {
       
         Pageable pageable = PageRequest.of(0, 10);
         Page<Place> page = new PageImpl<>(List.of(placeEntity));
         when(placeRepository.findByActiveTrue(pageable)).thenReturn(page);

         Page<PlaceResponseDTO> result = placeService.activesPlaces(pageable);
        
         assertNotNull(result);
         assertFalse(result.getContent().isEmpty());
         assertEquals(1, result.getTotalElements());
     }
}
