package com.cesde.eventhub.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	User haciaEntidad(UserRegisterDTO usuarioDTO);
	
	UserResponseDTO haciaDto(User usuario);
	
	default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getNameRole().name()) // Convierte el Enum a String (ej: "ADMIN")
                .collect(Collectors.toSet());
    }
}
