package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.LugarDTO;
import com.cesde.eventhub.modelos.Lugar;

@Mapper(componentModel = "spring")
public interface LugarMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	Lugar haciaEntidad(LugarDTO crearLugar);
	
	LugarDTO haciaDto(Lugar lugar);
}
