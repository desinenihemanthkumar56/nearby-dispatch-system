package com.nearby.dispatch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptOrderRequest {
    @NotNull
    private Long captainId;
}
