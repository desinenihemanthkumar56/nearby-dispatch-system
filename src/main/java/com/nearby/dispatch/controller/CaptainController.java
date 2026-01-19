package com.nearby.dispatch.controller;

import com.nearby.dispatch.dto.CaptainLocationRequest;
import com.nearby.dispatch.dto.CaptainStatusRequest;
import com.nearby.dispatch.dto.CreateCaptainRequest;
import com.nearby.dispatch.dto.NearbyOrderResponse;
import com.nearby.dispatch.entity.CaptainEntity;
import com.nearby.dispatch.service.CaptainService;
import com.nearby.dispatch.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/captains")
@RequiredArgsConstructor
public class CaptainController {

    private final CaptainService captainService;
    private final OrderService orderService;



    @PostMapping
    public CaptainEntity createCaptain(@Valid @RequestBody CreateCaptainRequest request) {
        return captainService.createCaptain(request);
    }
    @GetMapping("/{captainId}")
    public CaptainEntity getCaptain(@PathVariable Long captainId) {
        return captainService.getCaptain(captainId);
    }


    // ✅ Update Captain status ONLINE/OFFLINE
    @PostMapping("/{captainId}/status")
    public CaptainEntity updateStatus(
            @PathVariable Long captainId,
            @Valid @RequestBody CaptainStatusRequest request
    ) {
        return captainService.updateStatus(captainId, request);
    }

    // ✅ Update location
    @PostMapping("/{captainId}/location")
    public CaptainEntity updateLocation(
            @PathVariable Long captainId,
            @Valid @RequestBody CaptainLocationRequest request
    ) {
        return captainService.updateLocation(captainId, request);
    }

    // ✅ Get nearby orders
    @GetMapping("/{captainId}/nearby-orders")
    public List<NearbyOrderResponse> nearbyOrders(
            @PathVariable Long captainId,
            @RequestParam(defaultValue = "1") double radiusKm,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return orderService.getNearbyOrders(captainId, radiusKm, limit);
    }
}
