package com.nearby.dispatch.dto;

import com.nearby.dispatch.entity.Enums.CaptainStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaptainStatusRequest {
    @NotNull
    private CaptainStatus status;
}
