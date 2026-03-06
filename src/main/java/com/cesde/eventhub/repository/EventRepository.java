package com.cesde.eventhub.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.enums.Category;
import com.cesde.eventhub.enums.EventStatus;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
	List<Event> findAllByCategory(Category category);
	
	boolean existsByPlaceIdAndEventDateAndStatusNot(Long placeId, LocalDate eventDate, EventStatus status);
}