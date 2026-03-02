package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.request.UsuarioRegistroDTO;
import com.cesde.eventhub.dto.response.UsuarioRespuestaDTO;
import com.cesde.eventhub.modelos.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

	@Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "refreshToken", ignore = true)
	Usuario haciaEntidad(UsuarioRegistroDTO usuarioDTO);
	
	UsuarioRespuestaDTO haciaDto(Usuario usuario);
	
}
