package com.cesde.eventhub.service;

import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.eventhub.dto.MessageDTO;
import com.cesde.eventhub.dto.MyOrderDetailDTO;
import com.cesde.eventhub.dto.request.ConfirmPay;
import com.cesde.eventhub.dto.request.PhysicalSaleRequestDTO;
import com.cesde.eventhub.dto.request.PurchaseRequestDTO;
import com.cesde.eventhub.dto.response.CategoryReportDTO;
import com.cesde.eventhub.dto.response.GeneralReportResponseDTO;
import com.cesde.eventhub.dto.response.MyOrderDTO;
import com.cesde.eventhub.dto.response.OrderHistoryResponseDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.dto.response.TopEventDTO;
import com.cesde.eventhub.entity.Client;
import com.cesde.eventhub.entity.Event;
import com.cesde.eventhub.entity.Order;
import com.cesde.eventhub.entity.Ticket;
import com.cesde.eventhub.entity.TicketPrice;
import com.cesde.eventhub.entity.User;
import com.cesde.eventhub.enums.OrderFilter;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.enums.TicketStatus;
import com.cesde.eventhub.exception.custom.DataNotFound;
import com.cesde.eventhub.exception.custom.InvalidRegistration;
import com.cesde.eventhub.exception.custom.Unauthorized;
import com.cesde.eventhub.mapper.OrderMapper;
import com.cesde.eventhub.mapper.ReportMapper;
import com.cesde.eventhub.repository.ClientRepository;
import com.cesde.eventhub.repository.OrderRepository;
import com.cesde.eventhub.repository.TicketPriceRepository;
import com.cesde.eventhub.repository.TicketRepository;
import com.cesde.eventhub.utils.PaginationUtils;

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
    private final EventService eventService;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ReportMapper reportMapper;

    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public OrderResponseDTO createOrder(Long eventId, Long zoneId, PurchaseRequestDTO request) {
    	String user = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(user);
        
        Client client = clientService.findByUserId(userId);
               
        TicketPrice ticketPrice = ticketPriceRepository.findByEventIdAndZoneIdWithLock(eventId, zoneId)
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
        
        savedOrder.setTickets(tickets);
        ticketRepository.saveAll(tickets);
        orderRepository.flush();
        
        return orderMapper.toResponseDTO(savedOrder, expirationTime);
    }
    
    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public OrderResponseDTO confirmPayment(UUID orderId, ConfirmPay request) {
      
        UUID userId = userService.getAuthenticatedUserId();

        Order order = findById(orderId);

       validateOwner(userId, order);
        
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
        System.out.println(" Confirmación enviada al cliente: " + order.getClient().getUser().getEmail());


        return orderMapper.toResponseDTO(savedOrder, savedOrder.getExpirationDate());
    }

    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional
    public MessageDTO cancelOrder(UUID orderId) {
       
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(username);

        Order order = findById(orderId);

          validateOwner(userId, order);

        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new InvalidRegistration("Solo se pueden cancelar órdenes que estén en estado PENDIENTE.");
        }

        order.setStatus(OrderStatus.CANCELADA);
        
        order.getTickets().forEach(ticket -> {
            ticket.setStatus(TicketStatus.CANCELADA); 
            
            TicketPrice tp = ticket.getTicketPrice();
            tp.setAvailableQuantity(tp.getAvailableQuantity() + 1);
            ticketPriceRepository.save(tp);
        });

       // ticketRepository.deleteAll(order.getTickets());
        order.getTickets().clear();

       orderRepository.save(order);

       MessageDTO message = new MessageDTO("Boleta cancelada correctamente");
       return message;
      
    }
    
    
    @PreAuthorize("hasRole('VENDEDOR')")
    @Transactional
    public OrderResponseDTO createPhysicalSale(PhysicalSaleRequestDTO request) {
       
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID sellerId = UUID.fromString(username);
        User seller = userService.findById(sellerId);
                

        Event event = eventService.findEventById(request.getEventId());
               
        
        TicketPrice ticketPrice = ticketPriceRepository.findByEventIdAndZoneId(request.getEventId(), request.getZoneId())
                .orElseThrow(() -> new DataNotFound("La zona especificada no está configurada para este evento."));

       
        Client client = clientRepository.findByDocument(request.getBuyerDocument())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setDocument(request.getBuyerDocument());
                    newClient.setName(request.getBuyerName());
                    newClient.setPhone(request.getBuyerPhone());
                    newClient.setLastName(request.getBuyerLastName());
                    return clientRepository.save(newClient);
                });

       
        Order order = new Order();
        order.setClient(client);
        order.setSeller(seller); 
        order.setEvent(event);
        order.setStatus(OrderStatus.PAGADA); 
        order.setTotal(ticketPrice.getPrice() * request.getQuantity());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentReference("TAQUILLA-" + UUID.randomUUID().toString().substring(0, 8));
        order.setExpirationDate(LocalDateTime.now().plusYears(1)); 
        
        Order savedOrder = orderRepository.saveAndFlush(order);

        List<Ticket> tickets = IntStream.range(0, request.getQuantity())
                .mapToObj(i -> {
                    Ticket ticket = new Ticket();
                    ticket.setOrder(savedOrder);
                    ticket.setTicketPrice(ticketPrice);
                    ticket.setStatus(TicketStatus.ACTIVA); 
                    ticket.setCode(UUID.randomUUID()); 
                    return ticket;
                }).toList();
        
        ticketRepository.saveAll(tickets);

       
        
        System.out.println("Vendedor: " + seller.getEmail());
        System.out.println("Comprador: " + client.getName() + " - Doc: " + client.getDocument());
        System.out.println("Cantidad: " + request.getQuantity() + " boleta(s) en zona ID: " + request.getZoneId());
        System.out.println("Total Recaudado: $" + order.getTotal());

        return orderMapper.toResponseDTO(savedOrder, null); 
    }
    
    @PreAuthorize("hasRole('CLIENTE')")
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<MyOrderDTO> getMyOrders(OrderFilter filter, int page, int size) {
        UUID userId = userService.getAuthenticatedUserId();
        Client client = clientService.findByUserId(userId);
        UUID clientId = client.getId();
        LocalDate now = LocalDate.now();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orderPage;
        
        if (filter == OrderFilter.UPCOMING) {
            
        	orderPage = orderRepository.findByClientIdAndStatusAndEvent_EventDateGreaterThanEqual(
                    clientId, OrderStatus.PAGADA, now, pageable);
        } else if (filter == OrderFilter.PAST) {
           
        	orderPage = orderRepository.findByClientIdAndStatusAndEvent_EventDateLessThan(
                    clientId, OrderStatus.PAGADA, now, pageable);
        } else {
        	orderPage = orderRepository.findByClientIdAndStatus(
                    clientId, OrderStatus.PAGADA, pageable);
        }
        System.out.println("Cantidad de órdenes encontradas en DB: " + orderPage.getSize());
      
        return PaginationUtils.toPaginatedResponse(orderPage, orderMapper::toMyOrderDTO);
    }
    
    @PreAuthorize("hasRole('ORGANIZADOR')")
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<OrderHistoryResponseDTO> getOrganizerSalesHistory(
            Long eventId, OrderStatus status, LocalDate purchaseDate, int page, int size) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID organizerId = UUID.fromString(username);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        
        if (purchaseDate != null) {
            startDate = purchaseDate.atStartOfDay(); 
            endDate = purchaseDate.plusDays(1).atStartOfDay();
        }

        Page<Order> orderPage = orderRepository.findOrganizerSalesWithFilters(
                organizerId, eventId, status, startDate, endDate, pageable);

        return PaginationUtils.toPaginatedResponse(orderPage, orderMapper::toHistoryDTO);
    }
    
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<MyOrderDTO> getPendingOrders() {
        UUID userId = userService.getAuthenticatedUserId();
        
        Client cliente = clientService.findByUserId(userId);
        
        UUID clienteId = cliente.getId();
        System.out.println("Buscando órdenes pendientes para el usuario: " +  clienteId);
        List<Order> pendingOrders = orderRepository.findPendingOrdersByClientId(clienteId, OrderStatus.PENDIENTE);
        System.out.println("Cantidad de órdenes encontradas en DB: " + pendingOrders.size());
        
        if (!pendingOrders.isEmpty()) {
            System.out.println("ID de la primera orden real en Java: " + pendingOrders.get(0).getId());
        }
        
        List<MyOrderDTO> dtos = pendingOrders.stream()
                .map(orderMapper::toMyOrderDTO)
                .toList();
        
        System.out.println("Cantidad de DTOs después del mapper: " + dtos.size());
        return dtos;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public MyOrderDetailDTO getOrderDetails(UUID orderId) {
        UUID userId = userService.getAuthenticatedUserId();
        
        Client owner = validateOwner(userId, orderId);
        
        Order order = orderRepository.findByIdAndClientId(orderId, owner.getId())
                .orElseThrow(() -> new DataNotFound("La orden no existe o no te pertenece."));
                
        return orderMapper.toMyOrderDetailDTO(order);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public GeneralReportResponseDTO getGeneralReport(LocalDate startDate, LocalDate endDate) {
    	
    	if (startDate.isAfter(endDate)) {
            
            throw new InvalidRegistration("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        long totalOrders = orderRepository.countByStatusAndCreatedAtBetween(OrderStatus.PAGADA, start, end);
        long totalTickets = ticketRepository.countTicketsSoldInPeriod(start, end);
        Double rawRevenue = ticketRepository.sumRevenueInPeriod(start, end);
        double totalRevenue = rawRevenue != null ? rawRevenue : 0.0;

        List<CategoryReportDTO> categories = reportMapper.toCategoryDTOList(
                ticketRepository.getCategorySalesInPeriod(start, end));

        List<TopEventDTO> topEvents = reportMapper.toTopEventDTOList(
                ticketRepository.getTopEventsInPeriod(start, end, PageRequest.of(0, 5)));

        return reportMapper.toGeneralReportDTO(totalOrders, totalTickets, totalRevenue, categories, topEvents);
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
    
    public Order findById(UUID orderId) {
    	 Order order = orderRepository.findById(orderId)
                 .orElseThrow(() -> new DataNotFound("La orden especificada no existe."));
  
    return order;
    }
    
    public Client validateOwner(UUID userId, Order order) {
    
    	Client authenticatedClient = clientService.findByUserId(userId);
    	
    	if (!order.getClient().getId().equals(authenticatedClient.getId())) {
            throw new Unauthorized("No tienes permiso para acceder, modificar o cancelar esta orden.");
        }
    	return authenticatedClient;
    }
    
    public Client validateOwner(UUID userId, UUID orderId) {
        
    	Client authenticatedClient = clientService.findByUserId(userId);
    	Order order = findById(orderId);
    	
    	if (!order.getClient().getId().equals(authenticatedClient.getId())) {
            throw new Unauthorized("No tienes permiso para acceder, modificar o cancelar esta orden.");
        }
    	
    	return authenticatedClient;
    }
    
    
    
    
}
