package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.dto.response.PlaceResponseDTO;
import com.cesde.eventhub.entity.Place;


@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlaceMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	Place toEntity(PlaceDTO crearLugar);
	
	PlaceDTO toDTO(Place lugar);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Place toEntityUpdate(UpdatePlaceDTO updatePlace);
	
	 //use MappingTarget
	UpdatePlaceDTO toDTOUpdate(Place place);
	
	@Mapping(target = "id", ignore = true) 
    
    void updateEntityFromDTO(UpdatePlaceDTO dto, @MappingTarget Place place);
	
	PlaceResponseDTO toPage(Place place);
	
	
    
	
}
