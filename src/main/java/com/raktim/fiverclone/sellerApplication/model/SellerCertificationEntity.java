package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.common.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "seller_certifications")
public class SellerCertificationEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private SellerApplicationEntity application;

    @Column(name = "certification_name", nullable = false, length = 200)
    private String certificationName;

    @Column(name = "issuing_organization", length = 200)
    private String issuingOrganization;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "credential_id", length = 150)
    private String credentialId;

    @Column(name = "credential_url")
    private String credentialUrl;

    public void ensureBelongsTo(UUID applicationId) {
        if (!applicationId.equals(application.getId())) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "MISMATCH_SELLER_CERTIFICATION_AND_APPLICATION",
                    "Seller certification details mismatched found for this application."
            );
        }
    }
}
