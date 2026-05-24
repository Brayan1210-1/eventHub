package com.cesde.eventhub.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Ticket;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.projections.ZoneStatsProjection;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    Optional<Ticket> findByCode(UUID code);
    
 
    long countByOrder_EventIdAndOrder_Status(Long eventId, OrderStatus status);

    
    long countByOrder_EventIdAndStatus(Long eventId, TicketStatus status);

    @Query("SELECT SUM(t.ticketPrice.price) FROM Ticket t WHERE t.order.event.id = :eventId AND t.order.status = 'PAGADA'")
    Double sumRevenueByEventId(@Param("eventId") Long eventId);

   
    @Query("SELECT t.ticketPrice.zone.name as zoneName, COUNT(t) as ticketsSold, SUM(t.ticketPrice.price) as revenue " +
           "FROM Ticket t " +
           "WHERE t.order.event.id = :eventId AND t.order.status = 'PAGADA' " +
           "GROUP BY t.ticketPrice.zone.name")
    List<ZoneStatsProjection> getZoneStatsByEventId(@Param("eventId") Long eventId);
}