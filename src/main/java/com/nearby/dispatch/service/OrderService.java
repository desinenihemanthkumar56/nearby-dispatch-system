package com.nearby.dispatch.service;

import com.nearby.dispatch.dto.*;
import com.nearby.dispatch.entity.CaptainEntity;
import com.nearby.dispatch.entity.OrderEntity;
import com.nearby.dispatch.entity.OrderRejectionEntity;
import com.nearby.dispatch.entity.Enums.AssignmentAction;
import com.nearby.dispatch.entity.Enums.CaptainStatus;
import com.nearby.dispatch.entity.Enums.OrderStatus;
import com.nearby.dispatch.exception.ConflictException;
import com.nearby.dispatch.exception.NotFoundException;
import com.nearby.dispatch.repository.OrderRejectionRepository;
import com.nearby.dispatch.repository.OrderRepository;
import com.nearby.dispatch.util.GeoDistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRejectionRepository orderRejectionRepository;
    private final CaptainService captainService;
    private final AssignmentLogService assignmentLogService;

    public OrderEntity createOrder(CreateOrderRequest request) {
        OrderEntity order = OrderEntity.builder()
                .orderNo(request.getOrderNo())
                .pickupLat(request.getPickupLat())
                .pickupLng(request.getPickupLng())
                .status(OrderStatus.NEW)
                .createdAt(Instant.now())
                .build();

        return orderRepository.save(order);
    }

    public List<NearbyOrderResponse> getNearbyOrders(Long captainId, double radiusKm, int limit) {

        CaptainEntity captain = captainService.getCaptain(captainId);

        // Only ONLINE captains can see orders
        if (captain.getStatus() != CaptainStatus.ONLINE) {
            return List.of();
        }

        // Optional: stale location check (5 minutes)
        if (captain.getLastUpdatedAt() == null ||
                captain.getLastUpdatedAt().isBefore(Instant.now().minus(5, ChronoUnit.MINUTES))) {
            return List.of();
        }

        // Load NEW orders (Phase 1 simple)
        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatus.NEW);

        // Filter + calculate distance + apply rejection cooldown
        Instant now = Instant.now();

        List<NearbyOrderResponse> results = newOrders.stream()
                .map(order -> {
                    double dist = GeoDistanceUtil.distanceKm(
                            captain.getLat(), captain.getLng(),
                            order.getPickupLat(), order.getPickupLng()
                    );

                    return NearbyOrderResponse.builder()
                            .orderId(order.getId())
                            .orderNo(order.getOrderNo())
                            .pickupLat(order.getPickupLat())
                            .pickupLng(order.getPickupLng())
                            .distanceKm(dist)
                            .build();
                })
                .filter(res -> res.getDistanceKm() <= radiusKm)
                .filter(res -> !orderRejectionRepository.existsByOrderIdAndCaptainIdAndExpiresAtAfter(
                        res.getOrderId(), captainId, now
                ))
                .sorted(Comparator.comparingDouble(NearbyOrderResponse::getDistanceKm))
                .limit(limit)
                .toList();

        // (Optional) log that we SENT these orders to captain
        results.forEach(r ->
                assignmentLogService.log(r.getOrderId(), captainId, r.getDistanceKm(), AssignmentAction.SENT)
        );

        return results;
    }

    // ✅ Concurrency safe accept
    @Transactional
    public void acceptOrder(Long orderId, AcceptOrderRequest request) {

        Long captainId = request.getCaptainId();

        // ensure captain exists
        CaptainEntity captain = captainService.getCaptain(captainId);

        // ensure order exists
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND"));

        double distanceKm = GeoDistanceUtil.distanceKm(
                captain.getLat(), captain.getLng(),
                order.getPickupLat(), order.getPickupLng()
        );

        // 🔥 Atomic update
        int updated = orderRepository.acceptOrder(orderId, captainId);

        if (updated == 0) {
            assignmentLogService.log(orderId, captainId, distanceKm, AssignmentAction.TIMEOUT);
            throw new ConflictException("ORDER_ALREADY_ASSIGNED");
        }

        assignmentLogService.log(orderId, captainId, distanceKm, AssignmentAction.ACCEPTED);
    }

    @Transactional
    public void rejectOrder(Long orderId, RejectOrderRequest request) {

        Long captainId = request.getCaptainId();

        // ensure order exists
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND"));

        CaptainEntity captain = captainService.getCaptain(captainId);

        double distanceKm = GeoDistanceUtil.distanceKm(
                captain.getLat(), captain.getLng(),
                order.getPickupLat(), order.getPickupLng()
        );

        // Reject cooldown (example 5 minutes)
        Instant now = Instant.now();
        Instant expiresAt = now.plus(5, ChronoUnit.MINUTES);

        OrderRejectionEntity rejection = OrderRejectionEntity.builder()
                .orderId(orderId)
                .captainId(captainId)
                .rejectedAt(now)
                .expiresAt(expiresAt)
                .build();

        // save cooldown
        orderRejectionRepository.save(rejection);

        assignmentLogService.log(orderId, captainId, distanceKm, AssignmentAction.REJECTED);
    }
}
