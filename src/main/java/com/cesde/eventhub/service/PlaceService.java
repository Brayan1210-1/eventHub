package com.cesde.eventhub.service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.cesde.eventhub.dto.PlaceDTO;
import com.cesde.eventhub.dto.request.UpdatePlaceDTO;
import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.mapper.PlaceMapper;
import com.cesde.eventhub.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	
	private final PlaceMapper mapper;
	
	private final PlaceRepository lugarRepositorio;

	@Secured("ROLE_ADMIN")
	public PlaceDTO crearLugar(PlaceDTO lugar) {
		
		//if(lugar.getCapacidad_total() == null || lugar.getCiudad() == null || lugar.getDireccion() == null
			//	|| lugar.getNombre() == null) {
			//throw new RuntimeException("Los campos capacidad, ciudad, direccion y nombre no pueden ser nulos");
		//}
		
		Place lugarGuardar = mapper.haciaEntidad(lugar);
		lugarGuardar.setActivo(true);
		Place lugarGuardado = lugarRepositorio.save(lugarGuardar);
		
		return mapper.haciaDto(lugarGuardado);
	}
	
	@Secured("ROLE_ADMIN")
	public Page<Place> lugaresActivos(Pageable pageable){
	
		return lugarRepositorio.findByActivoTrue(pageable);
	}
	
	@Secured("ROLE_ADMIN")
	public UpdatePlaceDTO actualizarLugar(Long id, UpdatePlaceDTO lugarAct) {
		
		Optional<Place> lugarOpcional = lugarRepositorio.findById(id);
		
		if(!lugarOpcional.isPresent()) {
			throw new RuntimeException("No existe un lugar con el id: " + id);
		}
		
		//USAR MAPSTRUCT
		Place lugarEncontrado = lugarOpcional.get();
		
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
		
		Optional<Place> lugarABorrar = lugarRepositorio.findById(id);
		if(lugarABorrar.isEmpty()) {
			throw new RuntimeException("No se pudo encontrar el lugar con id: "+ id);
		}
		
		Place lugarBorrado = lugarABorrar.get();	
		lugarRepositorio.delete(lugarBorrado);
	}
	
	
}
