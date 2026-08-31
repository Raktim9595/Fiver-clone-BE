package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
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
@Table(name = "seller_portfolio_links")
public class SellerPortfolioEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private SellerApplicationEntity application;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_type", nullable = false, length = 40)
    private PortfolioLinkType linkType;

    @Column(length = 150)
    private String title;

    @Column(nullable = false, length = 2000)
    private String url;

    public void ensureBelongsTo(UUID applicationId) {
        if (!applicationId.equals(application.getId())) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "MISMATCH_SELLER_PORTFOLIO_AND_APPLICATION",
                    "Seller Portfolio details mismatched found for this application."
            );
        }
    }
}
