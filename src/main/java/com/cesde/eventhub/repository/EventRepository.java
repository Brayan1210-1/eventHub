package com.cesde.eventhub.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	/**@Query("SELECT e FROM Event e JOIN FETCH e.place WHERE e.status = 'PUBLICADO' " +
		       "AND (:category IS NULL OR e.category = :category) " +
		       "AND (:city IS NULL OR e.place.city = :city) " +
		       "AND (:startDate IS NULL OR e.eventDate >= :startDate) " +
		       "AND (:endDate IS NULL OR e.eventDate <= :endDate) " +
		       "ORDER BY e.eventDate ASC")
	    Page<Event> findPublicEvents(
	        @Param("category") Category category, 
	        @Param("city") String city, 
	        @Param("startDate") LocalDate startDate, 
	        @Param("endDate") LocalDate endDate, 
	        Pageable pageable
	    );
	    **/
}