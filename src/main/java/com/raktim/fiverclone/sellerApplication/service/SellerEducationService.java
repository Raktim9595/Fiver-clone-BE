package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerEducationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerEducationRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerEducationService {
    private final SellerEducationRepo repo;
    private final EntityReferenceResolver entityResolver;
    private final SellerApplicationMapper mapper;

    private record ValidationResult(
            SellerEducationEntity sellerEducationEntity,
            SellerApplicationEntity sellerApplicationEntity
    ) {}

    private static final Logger log =  LoggerFactory.getLogger(SellerEducationService.class);

    @Transactional
    public SellerEducationResponseDto create(
            UUID applicationId,
            SellerEducationRequestDto dto
    ) {
        log.info("Creating SellerEducationEntity {} of applicationId={}", dto, applicationId);

        SellerApplicationEntity sellerApplicationEntity = getApplication(applicationId);

        SellerEducationEntity newSellerEducationEntity =
                mapper.toSellerEducationEntity(dto, sellerApplicationEntity);

        repo.save(newSellerEducationEntity);
        log.info("Successfully saved seller education entity");

        return mapper.toSellerEducationResponseDto(newSellerEducationEntity);
    }

    @Transactional
    public SellerEducationResponseDto update(UUID id, UUID applicationId, SellerEducationRequestDto dto) {
        log.info("Updating SellerEducationEntity of applicationId={}", applicationId);

        ValidationResult validationResult = validateAndReturn(applicationId, id);
        SellerEducationEntity foundSellerEducationEntity = validationResult.sellerEducationEntity();

        mapper.updateSellerEducationFromDto(dto, foundSellerEducationEntity);
        log.info("Successfully updated seller education entity for applicationId={}", applicationId);
        return mapper.toSellerEducationResponseDto(foundSellerEducationEntity);
    }

    @Transactional
    public String delete(UUID id, UUID applicationId) {
        log.info("Deleting SellerEducationEntity of applicationId={}", applicationId);
        ValidationResult validationResult = validateAndReturn(applicationId, id);

        repo.delete(validationResult.sellerEducationEntity());
        log.info("successfully deleted SellerEductionEntity with id={} for applicationId={}",
                id, applicationId);
        return "Successfully deleted SellerEducationEntity %s for applicationId=%s".formatted(
                id, applicationId
        );
    }

    public SellerEducationEntity findById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    private ValidationResult validateAndReturn(UUID applicationId, UUID sellerEducationId) {
        SellerApplicationEntity application = getApplication(applicationId);
        SellerEducationEntity sellerEducation = entityResolver
                .getRequired(SellerEducationEntity.class, sellerEducationId);

        application.ensureEditableDuringReview();
        sellerEducation.ensureBelongsTo(applicationId);

        return new ValidationResult(sellerEducation, application);
    }

    private SellerApplicationEntity getApplication(UUID applicationId) {
        return entityResolver.getRequired(SellerApplicationEntity.class, applicationId);
    }
}
