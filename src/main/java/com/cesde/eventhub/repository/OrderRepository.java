package com.cesde.eventhub.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByEventId(Long eventId);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :limitTime")
    List<Order> findExpiredOrders(@Param("status") OrderStatus status, @Param("limitTime") LocalDateTime limitTime);
    
    
    
    Page<Order> findByClient_UserIdAndStatus(
            UUID userId, OrderStatus status, Pageable pageable);
   
    
    Page<Order> findByClient_UserIdAndStatusAndEvent_EventDateGreaterThanEqual(
            UUID userId, OrderStatus status, LocalDate date, Pageable pageable);

   
    Page<Order> findByClient_UserIdAndStatusAndEvent_EventDateLessThan(
            UUID userId, OrderStatus status, LocalDate date, Pageable pageable);
}