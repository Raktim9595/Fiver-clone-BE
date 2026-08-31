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

    private record ValidationResult(
            SellerPortfolioEntity sellerPortfolio,
            SellerApplicationEntity sellerApplication
    ) {}

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
        ValidationResult validationResult = validateAndReturn(id, applicationId);
        SellerPortfolioEntity foundPortfolioEntity = validationResult.sellerPortfolio();

        mapper.updateSellerPortfolioFromDto(dto, foundPortfolioEntity);
        log.info("Updated seller portfolio details for application {}", applicationId);

        return mapper.toSellerPortfolioResponseDto(foundPortfolioEntity);
    }

    @Transactional
    public String delete(UUID id, UUID applicationId) {
        log.info("Deleting seller portfolio details for application {}", id);
        ValidationResult validationResult = validateAndReturn(id, applicationId);

        repo.delete(validationResult.sellerPortfolio());
        log.info("Deleted seller portfolio details for application {}", applicationId);
        return "Successfully deleted seller portfolio with id=%s for application id = %s"
                .formatted(id, applicationId);
    }

    public SellerPortfolioEntity findById(UUID id) {
        log.info("Finding seller portfolio details for id={}", id);
        return repo.findById(id).orElse(null);
    }

    private ValidationResult validateAndReturn(UUID portfolioId, UUID applicationId) {
        SellerApplicationEntity application = getApplication(applicationId);
        SellerPortfolioEntity foundPortfolioEntity = entityReferenceResolver
                .getRequired(SellerPortfolioEntity.class, portfolioId);

        // validations
        application.ensureEditableDuringReview();
        foundPortfolioEntity.ensureBelongsTo(applicationId);

        return new ValidationResult(foundPortfolioEntity, application);
    }

    private SellerApplicationEntity getApplication(UUID id) {
        return entityReferenceResolver.getRequired(SellerApplicationEntity.class, id);
    }
}
