package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerPortfolioEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerPortfolioRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerPortfolioService {
    private final SellerPortfolioRepo repo;
    private final EntityReferenceResolver entityReferenceResolver;
    private final SellerApplicationMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(SellerPortfolioService.class);

    public SellerPortfolioResponseDto create(UUID applicationId, SellerPortfolioRequestDto dto) {
        log.info("Creating seller portfolio details {} for application {}", dto, applicationId);

        SellerApplicationEntity application = getApplication(applicationId);

        SellerPortfolioEntity newSellerPortfolio =
                mapper.toSellerPortfolioEntity(dto, application);

        SellerPortfolioEntity result = repo.save(newSellerPortfolio);

        log.info("Created seller portfolio details {} for application {}", dto, applicationId);
        return mapper.toSellerPortfolioResponseDto(result);
    }

    @Transactional
    public SellerPortfolioResponseDto update(
            UUID id,
            UUID applicationId,
            SellerPortfolioRequestDto dto
    ) {
        log.info("Updating seller portfolio details for application {}", id);
        SellerApplicationEntity application = getApplication(applicationId);
        SellerPortfolioEntity foundPortfolioEntity = entityReferenceResolver
                .getRequired(SellerPortfolioEntity.class, id);

        // validations
        application.ensureEditableDuringReview();
        foundPortfolioEntity.ensureBelongsTo(applicationId);

        mapper.updateSellerPortfolioFromDto(dto, foundPortfolioEntity);
        log.info("Updated seller portfolio details for application {}", applicationId);

        return mapper.toSellerPortfolioResponseDto(foundPortfolioEntity);
    }

    private SellerApplicationEntity getApplication(UUID id) {
        return entityReferenceResolver.getRequired(SellerApplicationEntity.class, id);
    }
}
