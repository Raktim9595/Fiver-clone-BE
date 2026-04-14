package com.raktim.fiverclone.seller.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelEntity;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sellers")
public class SellerEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity baseUser;

    // Adds the description or bio of the seller
    @Column(nullable = false)
    private String description;

    // What sort of experience level does the user holds.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "experience_id", nullable = false)
    private ExperienceLevelEntity experience;

    // What sort of skills does the user have
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "seller_skills",
            joinColumns = @JoinColumn(name = "seller_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<SkillEntity> skills = new HashSet<>();

    @Column(nullable = false)
    @Min(0)
    private Integer totalOrders;

    @Column(nullable = false)
    @Min(0)
    private BigDecimal totalEarnings;

    @PrePersist
    public void prePersist() {
        if (totalOrders == null) {
            totalOrders = 0;
        }

        if (totalEarnings == null) {
            totalEarnings = BigDecimal.ZERO;
        }
    }

}
