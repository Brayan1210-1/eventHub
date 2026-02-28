package com.cesde.eventhub.repositorio;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.modelos.Lugar;

@Repository
public interface LugarRepositorio extends JpaRepository<Lugar, Long>{

	public Page<Lugar> findByActivoTrue(Pageable pageable);
}
