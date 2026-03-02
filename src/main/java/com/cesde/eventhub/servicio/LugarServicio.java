package com.cesde.eventhub.servicio;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.LugarDTO;
import com.cesde.eventhub.dto.request.ActualizarLugarDTO;
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
		
		//if(lugar.getCapacidad_total() == null || lugar.getCiudad() == null || lugar.getDireccion() == null
			//	|| lugar.getNombre() == null) {
			//throw new RuntimeException("Los campos capacidad, ciudad, direccion y nombre no pueden ser nulos");
		//}
		
		Lugar lugarGuardar = mapper.haciaEntidad(lugar);
		lugarGuardar.setActivo(true);
		Lugar lugarGuardado = lugarRepositorio.save(lugarGuardar);
		
		return mapper.haciaDto(lugarGuardado);
	}
	
	@Secured("ROLE_ADMIN")
	public Page<Lugar> lugaresActivos(Pageable pageable){
	
		return lugarRepositorio.findByActivoTrue(pageable);
	}
	
	@Secured("ROLE_ADMIN")
	public ActualizarLugarDTO actualizarLugar(Long id, ActualizarLugarDTO lugarAct) {
		
		Optional<Lugar> lugarOpcional = lugarRepositorio.findById(id);
		
		if(!lugarOpcional.isPresent()) {
			throw new RuntimeException("No existe un lugar con el id: " + id);
		}
		
		//USAR MAPSTRUCT
		Lugar lugarEncontrado = lugarOpcional.get();
		
		lugarEncontrado.setNombre(lugarAct.getNombre());
		lugarEncontrado.setCiudad(lugarAct.getCiudad());
		lugarEncontrado.setCapacidad_total(lugarAct.getCapacidad_total());
		lugarEncontrado.setDescripcion(lugarAct.getDescripcion());
		lugarEncontrado.setDireccion(lugarAct.getDireccion());
		lugarEncontrado.setActivo(lugarAct.getActivo());
		lugarEncontrado.setImagenUrl(lugarAct.getImagenUrl());
		
		lugarRepositorio.save(lugarEncontrado);
		
		return mapper.haciaDTOAct(lugarEncontrado);
	}
	
	@Secured("ROLE_ADMIN")
	public void eliminarLugar(Long id) {
		
		Optional<Lugar> lugarABorrar = lugarRepositorio.findById(id);
		if(lugarABorrar.isEmpty()) {
			throw new RuntimeException("No se pudo encontrar el lugar con id: "+ id);
		}
		
		Lugar lugarBorrado = lugarABorrar.get();	
		lugarRepositorio.delete(lugarBorrado);
	}
	
	
}
