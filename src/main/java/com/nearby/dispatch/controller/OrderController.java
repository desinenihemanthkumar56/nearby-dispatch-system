package com.nearby.dispatch.controller;

import com.nearby.dispatch.dto.AcceptOrderRequest;
import com.nearby.dispatch.dto.CreateOrderRequest;
import com.nearby.dispatch.dto.RejectOrderRequest;
import com.nearby.dispatch.entity.OrderEntity;
import com.nearby.dispatch.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ Create NEW Order (simulating customer order service)
    @PostMapping
    public OrderEntity createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    // ✅ Accept order (Concurrency safe)
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody AcceptOrderRequest request
    ) {
        orderService.acceptOrder(orderId, request);
        return ResponseEntity.ok().body("ORDER_ACCEPTED");
    }

    // ✅ Reject order (cooldown)
    @PostMapping("/{orderId}/reject")
    public ResponseEntity<?> rejectOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody RejectOrderRequest request
    ) {
        orderService.rejectOrder(orderId, request);
        return ResponseEntity.ok().body("ORDER_REJECTED");
    }
}
