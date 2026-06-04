package com.cesde.eventhub.repository;

import java.time.LocalDate; 
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.enums.Category;
import com.cesde.eventhub.enums.EventStatus;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
	List<Event> findAllByCategory(Category category);
	
	boolean existsByPlaceIdAndEventDateAndStatusNot(Long placeId, LocalDate eventDate, EventStatus status);
	
	@Query("SELECT e FROM Event e JOIN FETCH e.place p WHERE p.active = true")
	Page<Event> findAllWithActivePlace(Pageable pageable);
	
	@EntityGraph(attributePaths = {"ticketPrices", "place"})
	@Query("SELECT e FROM Event e " +
	           "WHERE e.status = 'PUBLICADO' " + 
	           "AND (cast(:category as string) IS NULL OR e.category = :category) " +
	           "AND (cast(:city as string) IS NULL OR LOWER(e.place.city) LIKE LOWER(CONCAT('%', cast(:city as string), '%')))" +
	           "AND (cast(:startingDate as localdate)  IS NULL OR e.eventDate >= :startingDate) " +
	           "AND (cast(:endDate as localdate) IS NULL OR e.eventDate <= :endDate)")
	    Page<Event> filterEventsPublics(
	        @Param("category") Category category, 
	        @Param("city") String city, 
	        @Param("startingDate") LocalDate startingDate, 
	        @Param("endDate") LocalDate endDate,
	        Pageable pageable);

	@EntityGraph(attributePaths = {"place", "ticketPrices", "ticketPrices.zone"})
	@Query("SELECT e FROM Event e WHERE e.id = :id AND e.status = com.cesde.eventhub.enums.EventStatus.PUBLICADO")
    Optional<Event> findWithDetailsById(Long id);
	
	@Query("SELECT e FROM Event e " +
	           "WHERE e.organizer.id = :userId " +
	           "AND (:status IS NULL OR e.status = :status)")
	    Page<Event> findMyEventsWithFilters(
	            @Param("userId") UUID userId, 
	            @Param("status") EventStatus status, 
	            Pageable pageable);

}