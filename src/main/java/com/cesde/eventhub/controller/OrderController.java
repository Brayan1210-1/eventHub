package com.cesde.eventhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.eventhub.dto.request.PurchaseRequestDTO;
import com.cesde.eventhub.dto.response.OrderResponseDTO;
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
}