package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import com.raktim.fiverclone.sellerApplication.enums.SellerAccountStatus;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "seller_profile")
public class SellerProfileEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "approved_application_id",
            nullable = false,
            unique = true
    )
    private SellerApplicationEntity approvedApplication;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(
            name = "professional_headline",
            nullable = false,
            length = 120
    )
    private String professionalHeadline;

    @Column(nullable = false, length = 600)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(name = "professional_level", nullable = false, length = 30)
    private ExperienceLevel professionalLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private OccupationEntity occupation;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 30)
    private SellerAccountStatus accountStatus = SellerAccountStatus.ACTIVE;

    @Builder.Default
    @Column(
            name = "average_rating",
            nullable = false,
            precision = 3,
            scale = 2
    )
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_reviews", nullable = false)
    private Long totalReviews = 0L;

    @Builder.Default
    @Column(name = "total_orders", nullable = false)
    private Long totalOrders = 0L;

    @Builder.Default
    @Column(name = "completed_orders", nullable = false)
    private Long completedOrders = 0L;
}
