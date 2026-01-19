package com.nearby.dispatch.service;

import com.nearby.dispatch.dto.CaptainLocationRequest;
import com.nearby.dispatch.dto.CaptainStatusRequest;
import com.nearby.dispatch.dto.CreateCaptainRequest;
import com.nearby.dispatch.entity.CaptainEntity;
import com.nearby.dispatch.entity.Enums.CaptainStatus;
import com.nearby.dispatch.exception.BadRequestException;
import com.nearby.dispatch.exception.NotFoundException;
import com.nearby.dispatch.repository.CaptainRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
@Builder
@Service
@RequiredArgsConstructor
public class CaptainService {

    private final CaptainRepository captainRepository;

    public CaptainEntity getCaptain(Long captainId) {
        return captainRepository.findById(captainId)
                .orElseThrow(() -> new NotFoundException("CAPTAIN_NOT_FOUND"));
    }

    public CaptainEntity updateStatus(Long captainId, CaptainStatusRequest request) {
        CaptainEntity captain = getCaptain(captainId);
        captain.setStatus(request.getStatus());
        captain.setLastUpdatedAt(Instant.now());
        return captainRepository.save(captain);
    }
    public CaptainEntity createCaptain(CreateCaptainRequest request) {

        validateLatLng(request.getLat(), request.getLng());

        CaptainEntity captain = CaptainEntity.builder()
                .name(request.getName())
                .status(request.getStatus())
                .lat(request.getLat())
                .lng(request.getLng())
                .activeOrdersCount(0)
                .lastUpdatedAt(Instant.now())
                .build();

        return captainRepository.save(captain);
    }

    public CaptainEntity updateLocation(Long captainId, CaptainLocationRequest request) {
        validateLatLng(request.getLat(), request.getLng());

        CaptainEntity captain = getCaptain(captainId);

        if (captain.getStatus() == CaptainStatus.OFFLINE) {
            // Optional: allow location updates but won't show orders
            // throw new BadRequestException("CAPTAIN_OFFLINE_CANNOT_UPDATE_LOCATION");
        }

        captain.setLat(request.getLat());
        captain.setLng(request.getLng());
        captain.setLastUpdatedAt(Instant.now());

        return captainRepository.save(captain);
    }

    private void validateLatLng(Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new BadRequestException("LAT_LNG_REQUIRED");
        }
        if (lat < -90 || lat > 90) {
            throw new BadRequestException("INVALID_LATITUDE");
        }
        if (lng < -180 || lng > 180) {
            throw new BadRequestException("INVALID_LONGITUDE");
        }
    }
}
