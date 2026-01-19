package com.nearby.dispatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String orderNo;

    @NotNull
    private Double pickupLat;

    @NotNull
    private Double pickupLng;
}
