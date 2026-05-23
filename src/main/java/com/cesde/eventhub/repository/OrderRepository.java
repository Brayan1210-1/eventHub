package com.cesde.eventhub.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEventId(Long eventId);
    
    //eliminar las órdenes expiradas
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :limitTime")
    List<Order> findExpiredOrders(@Param("status") OrderStatus status, @Param("limitTime") LocalDateTime limitTime);
}