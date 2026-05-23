package com.cesde.eventhub.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.request.ConfirmPay;
import com.cesde.eventhub.dto.request.PurchaseRequestDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.entity.Ticket;
import com.cesde.eventhub.entity.TicketPrice;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.exception.custom.Unauthorized;
import com.cesde.eventhub.mapper.OrderMapper;
import com.cesde.eventhub.repository.OrderRepository;
import com.cesde.eventhub.repository.TicketPriceRepository;
import com.cesde.eventhub.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final TicketPriceRepository ticketPriceRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;

    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public OrderResponseDTO createOrder(PurchaseRequestDTO request) {
    	String user = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(user);
        
        User client = userService.findById(userId);
               
        TicketPrice ticketPrice = ticketPriceRepository.findByEventIdAndZoneIdWithLock(request.getEventId(), request.getZoneId())
                .orElseThrow(() -> new DataNotFound("Zona o evento no disponible"));

        Event event = ticketPrice.getEvent();

        LocalDateTime now = LocalDateTime.now();
        if (event.getSalesStartDate() == null || event.getSalesEndDate() == null ||
                now.isBefore(event.getSalesStartDate()) || now.isAfter(event.getSalesEndDate())) {
            throw new InvalidRegistration("Las ventas para este evento están cerradas.");
        }

        
        if (ticketPrice.getAvailableQuantity() < request.getQuantity()) {
            throw new InvalidRegistration("No hay suficientes boletas disponibles.");
        }

        LocalDateTime expirationTime = now.plusMinutes(10);
        
        
        Order order = new Order();
        order.setClient(client);
        order.setEvent(event);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setExpirationDate(expirationTime);
        order.setTotal(ticketPrice.getPrice() * request.getQuantity());
    
        Order savedOrder = orderRepository.save(order);

        ticketPrice.setAvailableQuantity(ticketPrice.getAvailableQuantity() - request.getQuantity());
        ticketPriceRepository.save(ticketPrice);

        List<Ticket> tickets = IntStream.range(0, request.getQuantity())
                .mapToObj(i -> {
                    Ticket ticket = new Ticket();
                    ticket.setOrder(savedOrder);
                    ticket.setTicketPrice(ticketPrice);
                    ticket.setStatus(TicketStatus.RESERVADO);
                    return ticket;
                }).collect(Collectors.toList());
        ticketRepository.saveAll(tickets);

        
        return orderMapper.toResponseDTO(savedOrder, expirationTime);
    }
    
    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public OrderResponseDTO confirmPayment(UUID orderId, ConfirmPay request) {
      
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(username);

        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFound("La orden especificada no existe."));

        if (!order.getClient().getId().equals(userId)) {
            throw new Unauthorized("No tienes permiso para modificar esta orden.");
        }
        
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new InvalidRegistration("La orden no está en estado PENDIENTE.");
        }

        if (LocalDateTime.now().isAfter(order.getExpirationDate())) {
            throw new InvalidRegistration("El tiempo para pagar esta orden ha expirado.");
        }

        order.setStatus(OrderStatus.PAGADA);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentReference(request.getPaymentReference());

        
        order.getTickets().forEach(ticket -> {
            ticket.setCode(UUID.randomUUID());
            ticket.setStatus(TicketStatus.ACTIVA); 
        });

        Order savedOrder = orderRepository.save(order);

        System.out.println(" PAGO CONFIRMADO EXITOSAMENTE");
        System.out.println(" Orden ID: " + savedOrder.getId());
        System.out.println(" Referencia: " + savedOrder.getPaymentReference());
        System.out.println(" Confirmación enviada al cliente: " + order.getClient().getEmail());


        return orderMapper.toResponseDTO(savedOrder, savedOrder.getExpirationDate());
    }

    
    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredTickets() {
        LocalDateTime expirationThreshold = LocalDateTime.now().minusMinutes(10);
        
        List<Order> expiredOrders = orderRepository.findExpiredOrders(OrderStatus.PENDIENTE, expirationThreshold);

        if (expiredOrders.isEmpty()) {
            return;
        }

        log.info("Iniciando limpieza automática de {} órdenes expiradas...", expiredOrders.size());

        expiredOrders.forEach(order -> {
            log.info("Cancelando orden ID: {} por límite de tiempo de reserva alcanzado.", order.getId());
            
           
            order.setStatus(OrderStatus.CANCELADA); 
            
            order.getTickets().forEach(ticket -> {
                ticket.setStatus(TicketStatus.CANCELADA); 
                
             
                TicketPrice tp = ticket.getTicketPrice();
                tp.setAvailableQuantity(tp.getAvailableQuantity() + 1);
                ticketPriceRepository.save(tp);
            });
        });

        orderRepository.saveAll(expiredOrders);
        log.info("Limpieza de inventario temporal completada exitosamente.");
    }
}
