package com.raktim.fiverclone.seller.model;

import com.raktim.fiverclone.seller.enums.SellerApplicationStatus;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "seller_application_status_history")
public class SellerApplicationStatusHistoryEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private SellerApplicationEntity application;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private SellerApplicationStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private SellerApplicationStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private UserEntity changedBy;

    @Column(length = 2000)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
