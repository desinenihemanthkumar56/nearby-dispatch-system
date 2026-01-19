package com.nearby.dispatch.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyOrderResponse {
    private Long orderId;
    private String orderNo;
    private Double pickupLat;
    private Double pickupLng;
    private Double distanceKm;
}
