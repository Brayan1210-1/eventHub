package com.cesde.eventhub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cesde.eventhub.entity.TicketPrice;

import jakarta.persistence.LockModeType;

public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
	
	List<TicketPrice> findAllByEventId(Long eventId);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT tp FROM TicketPrice tp WHERE tp.event.id = :eventId AND tp.zone.id = :zoneId")
    Optional<TicketPrice> findByEventIdAndZoneIdWithLock(@Param("eventId") Long eventId, @Param("zoneId") Long zoneId);

}
