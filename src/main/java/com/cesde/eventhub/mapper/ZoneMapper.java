package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.ZoneRegisterDTO;
import com.cesde.eventhub.dto.response.ZoneResponseDTO;
import com.cesde.eventhub.entity.Zone;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ZoneMapper {

	
	public Zone toEntity(ZoneRegisterDTO zone);
	
	@Mapping(target = "place", source = "place.id")
	public ZoneResponseDTO toDTO(Zone zone);
	
	@Mapping(target = "id", ignore = true) 
    @Mapping(target = "place", ignore = true) 
    void updateEntityFromDTO(ZoneRegisterDTO dto, @MappingTarget Zone zone);
	
}
