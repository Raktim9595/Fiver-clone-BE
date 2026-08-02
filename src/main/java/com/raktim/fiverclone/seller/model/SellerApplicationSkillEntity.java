package com.raktim.fiverclone.seller.model;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "seller_applicaion_skills")
public class SellerApplicationSkillEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private SellerApplicationEntity application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillEntity skill;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", length = 30)
    private ExperienceLevel proficiencyLevel;
}
