package com.nearby.dispatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "order_rejection",
        uniqueConstraints = @UniqueConstraint(columnNames = {"orderId", "captainId"})
)
public class OrderRejectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long captainId;

    @Column(nullable = false)
    private Instant rejectedAt;

    @Column(nullable = false)
    private Instant expiresAt;
}
