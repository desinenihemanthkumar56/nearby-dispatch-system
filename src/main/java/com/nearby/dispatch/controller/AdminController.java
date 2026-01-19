package com.nearby.dispatch.controller;

import com.nearby.dispatch.entity.OrderEntity;
import com.nearby.dispatch.entity.Enums.OrderStatus;
import com.nearby.dispatch.repository.AssignmentLogRepository;
import com.nearby.dispatch.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final AssignmentLogRepository assignmentLogRepository;

    // ✅ Get orders by status
    @GetMapping("/orders")
    public List<OrderEntity> getOrders(@RequestParam(defaultValue = "NEW") OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    // ✅ Get order by ID
    @GetMapping("/orders/{orderId}")
    public OrderEntity getOrder(@PathVariable Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    // ✅ Get assignment logs by orderId
    @GetMapping("/assignment-logs")
    public Object logs(@RequestParam Long orderId) {
        return assignmentLogRepository.findByOrderId(orderId);
    }
}
