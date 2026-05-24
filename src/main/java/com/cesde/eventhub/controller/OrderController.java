package com.cesde.eventhub.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.ConfirmPay;
import com.cesde.eventhub.dto.request.PhysicalSaleRequestDTO;
import com.cesde.eventhub.dto.request.PurchaseRequestDTO;
import com.cesde.eventhub.dto.response.MyOrderDTO;
import com.cesde.eventhub.dto.response.OrderHistoryResponseDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
import com.cesde.eventhub.dto.response.PaginatedResponseDTO;
import com.cesde.eventhub.enums.OrderFilter;
import com.cesde.eventhub.enums.OrderStatus;
import com.cesde.eventhub.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/comprar")
    public ResponseEntity<OrderResponseDTO> purchaseTickets(@Valid @RequestBody PurchaseRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }
    
    @PostMapping("/{id}/confirmar-pago")
    public ResponseEntity<OrderResponseDTO> confirmPayment(
            @PathVariable UUID id, 
            @Valid @RequestBody ConfirmPay request) {
        
        return ResponseEntity.ok(orderService.confirmPayment(id, request));
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<OrderResponseDTO> cancelOrder(
            @PathVariable UUID id) {
        
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
    
    @PostMapping("/venta-fisica")
    public ResponseEntity<OrderResponseDTO> createPhysicalSale(
            @Valid @RequestBody PhysicalSaleRequestDTO request) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createPhysicalSale(request));
    }
    
    @GetMapping("/mis-boletas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PaginatedResponseDTO<MyOrderDTO>> getMyOrders(
            @RequestParam(required = false, defaultValue = "UPCOMING") OrderFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) { 
        
        return ResponseEntity.ok(orderService.getMyOrders(filter, page, size));
    }
    
}