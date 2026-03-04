package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Client;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface ClientMapper {

	Client toEntity(UserRegisterDTO userDTO);
	
	UserResponseDTO toDTO(Client client);
}
