package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.entity.Place;

@Mapper(componentModel = "spring")
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
	Place toEntityUpdate(UpdatePlaceDTO actualizarLugar);
	
	UpdatePlaceDTO toDTOUpdate(Place lugar);
	
     //usar MappingTarget
	
}
