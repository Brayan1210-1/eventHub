package com.cesde.eventhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.eventhub.entity.TicketPrice;

public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
	
	List<TicketPrice> findAllByEventId(Long eventId);

}
