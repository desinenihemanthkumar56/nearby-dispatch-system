package com.nearby.dispatch.dto;

import com.nearby.dispatch.entity.Enums.CaptainStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCaptainRequest {

    @NotBlank
    private String name;

    @NotNull
    private CaptainStatus status; // ONLINE / OFFLINE

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
