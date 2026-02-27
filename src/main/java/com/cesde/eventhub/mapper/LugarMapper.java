package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.CrearLugarDTO;
import com.cesde.eventhub.modelos.Lugar;

@Mapper(componentModel = "spring")
public interface LugarMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	Lugar haciaEntidad(CrearLugarDTO crearLugar);
	
	CrearLugarDTO haciaDto(Lugar lugar);
}
