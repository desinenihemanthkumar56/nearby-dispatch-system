package com.nearby.dispatch.repository;

import com.nearby.dispatch.entity.AssignmentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentLogRepository extends JpaRepository<AssignmentLogEntity, Long> {
    List<AssignmentLogEntity> findByOrderId(Long orderId);
}
