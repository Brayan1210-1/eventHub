package com.cesde.eventhub.servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.LugarDTO;
import com.cesde.eventhub.mapper.LugarMapper;
import com.cesde.eventhub.modelos.Lugar;
import com.cesde.eventhub.repositorio.LugarRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LugarServicio {
	
	private final LugarMapper mapper;
	
	private final LugarRepositorio lugarRepositorio;

	@Secured("ROLE_ADMIN")
	public LugarDTO crearLugar(LugarDTO lugar) {
		
		Lugar lugarGuardar = mapper.haciaEntidad(lugar);
		lugarGuardar.setActivo(true);
		Lugar lugarGuardado = lugarRepositorio.save(lugarGuardar);
		
		return mapper.haciaDto(lugarGuardado);
	}
	
	@Secured("ROLE_ADMIN")
	public Page<Lugar> lugaresActivos(Pageable pageable){
		
		
		return lugarRepositorio.findByActivoTrue(pageable);
	}
}
