package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seller_professional_profiles")
public class SellerProfessionalProfileEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", unique = true)
    private SellerApplicationEntity application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "occupation_id", nullable = false)
    private OccupationEntity occupation;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false, length = 30)
    private ExperienceLevel professionalLevel;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;
}
