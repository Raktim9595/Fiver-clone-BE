package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.language.model.LanguageEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "seller_personal_profiles")
public class SellerPersonalProfileEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id")
    private SellerApplicationEntity application;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName; // make it same name as username in the beginning

    @Column(
            name = "professional_headline",
            nullable = false,
            length = 120
    )
    private String professionalHeadline;

    @Column(nullable = false, length = 600)
    private String description; //Make the description same as bio section in the evening

    @Column(nullable = false, length = 25)
    private String country;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "seller_application_languages",
            joinColumns = @JoinColumn(name = "seller_personal_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<LanguageEntity> languages = new HashSet<>();
}
