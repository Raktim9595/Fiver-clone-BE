package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "seller_application_status_history")
public class SellerApplicationStatusHistoryEntity extends BaseEntity {
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
}
