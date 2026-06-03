package com.cesde.eventhub.repository;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Place;
import com.cesde.eventhub.projections.PlaceBasicProjection;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>{

	public Page<Place> findByActiveTrue(Pageable pageable);
	
	public Optional<Place> findById(Long id);
	
	
    @Query("SELECT p.id as id, p.name as name, p.city as city, " +
           "COUNT(z.id) as totalZones, SUM(z.capacity) as totalCapacityZones " +
           "FROM Place p JOIN p.zones z " +
           "WHERE p.active = true " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(CAST(:name AS string), ''), '%')))" +
           "GROUP BY p.id, p.name, p.city")
    Page<PlaceBasicProjection> findActiveBasicPlacesWithFilters(@Param("name") String name, Pageable pageable);
}
