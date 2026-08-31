package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.common.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.UUID;

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

    public void ensureBelongsTo(UUID applicationId) {
        if (!applicationId.equals(application.getId())) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "MISMATCH_SELLER_EDUCATION_AND_APPLICATION",
                    "Seller education details mismatched found for this application."
            );
        }
    }
}
