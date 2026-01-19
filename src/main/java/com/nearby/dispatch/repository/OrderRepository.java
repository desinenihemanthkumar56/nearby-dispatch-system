package com.nearby.dispatch.repository;

import com.nearby.dispatch.entity.OrderEntity;
import com.nearby.dispatch.entity.Enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus status);

    @Modifying
    @Query("""
        UPDATE OrderEntity o
        SET o.status = 'ASSIGNED',
            o.assignedCaptainId = :captainId
        WHERE o.id = :orderId
          AND o.status = 'NEW'
    """)
    int acceptOrder(@Param("orderId") Long orderId,
                    @Param("captainId") Long captainId);

    @Modifying
    @Query("""
        UPDATE OrderEntity o
        SET o.status = :newStatus
        WHERE o.id = :orderId
    """)
    int updateStatus(@Param("orderId") Long orderId,
                     @Param("newStatus") OrderStatus newStatus);

    List<OrderEntity> findByStatusAndCreatedAtAfter(OrderStatus status, Instant after);
}
