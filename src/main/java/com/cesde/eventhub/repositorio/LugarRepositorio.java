package com.cesde.eventhub.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.modelos.Lugar;

@Repository
public interface LugarRepositorio extends JpaRepository<Lugar, Long>{

	public List<Lugar> findByActivoTrue();
}
