package com.cesde.eventhub.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Zone;


@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long>{

	public List<Zone> findByPlaceId(Long id);
	
	@Query("SELECT SUM(z.capacity) FROM Zone z WHERE z.place.id = :placeId")
    Integer sumCapacityByPlaceId(@Param("placeId") Long placeId);
	
	Page<Zone> findByPlaceId(Long placeId, Pageable pageable);
}
