package com.raktim.fiverclone.seller.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seller_education")
public class SellerEducationEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private SellerApplicationEntity application;

    @Column(name = "institution_name", nullable = false, length = 200)
    private String institutionName;

    @Column(name = "country", length = 20)
    private String country;

    @Column(length = 150)
    private String degree;

    @Column(name = "field_of_study", length = 150)
    private String fieldOfStudy;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Builder.Default
    @Column(name = "is_current", nullable = false)
    private Boolean current = false;
}
