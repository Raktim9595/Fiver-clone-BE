package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "seller_applications")
public class SellerApplicationEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SellerApplicationStatus status = SellerApplicationStatus.DRAFT;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "current_step", nullable = false, length = 40)
    private SellerOnboardingSteps currentStep =
            SellerOnboardingSteps.PERSONAL_PROFILE;

    @Builder.Default
    @Column(name = "completion_percentage")
    private Integer completionPercentage = 0;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "review_started_at")
    private Instant reviewStartedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "rejected_at")
    private Instant rejectedAt;

    @OneToOne(
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private SellerPersonalProfileEntity personalProfile;

    @OneToOne(
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private SellerProfessionalProfileEntity professionalProfile;

    @Builder.Default
    @OneToMany(
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SellerEducationEntity> educationRecords = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "application",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<SellerPortfolioEntity> portfolios = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "seller_application_languages",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "seller_application_skills",
            joinColumns = @JoinColumn(name = "application_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillEntity> skills = new HashSet<>();
}
