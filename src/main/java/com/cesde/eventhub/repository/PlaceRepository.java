package com.cesde.eventhub.repository;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>{

	public Page<Place> findByActivoTrue(Pageable pageable);
	
	public Optional<Place> findById(Long id);
}
