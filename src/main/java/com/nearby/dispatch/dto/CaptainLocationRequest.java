package com.nearby.dispatch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaptainLocationRequest {

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
