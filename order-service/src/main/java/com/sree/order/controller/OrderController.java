package com.sree.order.controller;

import com.sree.order.dto.OrderRequestDTO;
import com.sree.order.dto.OrderResponseDTO;
import com.sree.order.entity.PurchaseOrder;
import com.sree.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Mono<PurchaseOrder> createOrder(@RequestBody OrderRequestDTO orderMono) {
        return orderService.createOrder(orderMono);
    }

    @GetMapping("/all")
    public Flux<OrderResponseDTO> getAllPurchasedOrders() {
        return orderService.getAllOrders();
    }

}
