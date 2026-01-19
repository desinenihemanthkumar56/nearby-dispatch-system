package com.nearby.dispatch.repository;

import com.nearby.dispatch.entity.OrderRejectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface OrderRejectionRepository extends JpaRepository<OrderRejectionEntity, Long> {

    boolean existsByOrderIdAndCaptainIdAndExpiresAtAfter(Long orderId, Long captainId, Instant now);
}
