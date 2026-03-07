package com.cesde.eventhub.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.UserRegisterDTO;
import com.cesde.eventhub.dto.response.UserResponseDTO;
import com.cesde.eventhub.entity.Role;
import com.cesde.eventhub.entity.User;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface UserMapper {

	
	User toEntity(UserRegisterDTO usuarioDTO);
	
	UserResponseDTO toDTO(User usuario);
	
	default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getNameRole().name()) // Convierte el Enum a String (ej: "ADMIN")
                .collect(Collectors.toSet());
    }
}
