package com.raktim.fiverclone.sellerApplication.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
import jakarta.persistence.*;
import lombok.*;

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
}
