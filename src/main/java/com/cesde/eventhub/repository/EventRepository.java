package com.cesde.eventhub.repository;

import java.time.LocalDate;
import java.util.List;

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
	List<Event> findAllWithActivePlace();
	
	@EntityGraph(attributePaths = {"ticketPrices", "place"})
	@Query("SELECT e FROM Event e " +
	           "WHERE e.status = 'PUBLICADO' " + 
	           "AND (cast(:category as string) IS NULL OR e.category = :category) " +
	           "AND (cast(:city as string) IS NULL OR e.place.city = :city) " +
	           "AND (cast(:startingDate as localdate)  IS NULL OR e.eventDate >= :startingDate) " +
	           "AND (cast(:endDate as localdate) IS NULL OR e.eventDate <= :endDate)")
	    Page<Event> filterEventsPublics(
	        @Param("category") Category category, 
	        @Param("city") String city, 
	        @Param("startingDate") LocalDate startingDate, 
	        @Param("endDate") LocalDate endDate,
	        Pageable pageable);


}