package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	User haciaEntidad(UserRegisterDTO usuarioDTO);
	
	UserResponseDTO haciaDto(User usuario);
	
}
