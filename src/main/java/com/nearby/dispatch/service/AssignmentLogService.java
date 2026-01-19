package com.nearby.dispatch.service;

import com.nearby.dispatch.entity.AssignmentLogEntity;
import com.nearby.dispatch.entity.Enums.AssignmentAction;
import com.nearby.dispatch.repository.AssignmentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AssignmentLogService {

    private final AssignmentLogRepository assignmentLogRepository;

    public void log(Long orderId, Long captainId, double distanceKm, AssignmentAction action) {
        AssignmentLogEntity log = AssignmentLogEntity.builder()
                .orderId(orderId)
                .captainId(captainId)
                .distanceKm(distanceKm)
                .action(action)
                .createdAt(Instant.now())
                .build();

        assignmentLogRepository.save(log);
    }
}
