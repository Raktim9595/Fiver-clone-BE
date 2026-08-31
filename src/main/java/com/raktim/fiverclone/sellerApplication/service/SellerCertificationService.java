package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerCertificationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerCertificationRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerCertificationService {
    private final SellerCertificationRepo repo;
    private final EntityReferenceResolver entityReferenceResolver;
    private final SellerApplicationMapper mapper;
    private record ValidationResult(
            SellerApplicationEntity application,
            SellerCertificationEntity sellerCertification
    ) {}

    private static final Logger log =  LoggerFactory.getLogger(SellerCertificationService.class);

    @Transactional
    public SellerCertificationResponseDto create(UUID applicationId, SellerCertificationRequestDto dto) {
        log.info("Creating Seller Certification {} with applicationId {}", dto, applicationId);

        SellerApplicationEntity application = getApplication(applicationId);

        SellerCertificationEntity newEntity =
                mapper.toSellerCertificationEntity(dto, application);

        SellerCertificationEntity result = repo.save(newEntity);

        log.info("Created Seller Certification {} with applicationId {}", dto, applicationId);

        return mapper.toSellerCertificationResponseDto(result);
    }

    @Transactional
    public SellerCertificationResponseDto update(UUID id, UUID applicationId, SellerCertificationRequestDto dto) {
        log.info("Updating Seller Certification with applicationId {}", applicationId);

        ValidationResult validationResult = validateAndReturn(applicationId, id);
        SellerCertificationEntity foundSellerCertificationEntity = validationResult.sellerCertification();

        mapper.updateSellerCertificationFromDto(dto, foundSellerCertificationEntity);
        log.info("Successfully updated Seller Certification with applicationId {}", applicationId);

        return mapper.toSellerCertificationResponseDto(foundSellerCertificationEntity);
    }

    @Transactional
    public String delete(UUID id, UUID applicationId) {
        log.info("Deleting Seller Certification with applicationId {}", applicationId);
        ValidationResult validationResult = validateAndReturn(applicationId, id);
        SellerCertificationEntity foundSellerCertificationEntity = validationResult.sellerCertification();
        repo.delete(foundSellerCertificationEntity);
        log.info("Successfully deleted Seller Certification with applicationId {}", applicationId);

        return "Successfully deleted Seller Certification with applicationId %s and certificationId %s"
                .formatted(applicationId, id);
    }

    public SellerCertificationEntity findById(UUID id) {
        log.info("Finding Seller Certification with applicationId {}", id);
        return repo.findById(id).orElse(null);
    }

    private ValidationResult validateAndReturn(UUID applicationId, UUID certificationId) {
        SellerApplicationEntity application = getApplication(applicationId);
        SellerCertificationEntity foundSellerCertificationEntity =
                entityReferenceResolver.getRequired(SellerCertificationEntity.class, certificationId);

        // validations
        application.ensureEditableDuringReview();
        foundSellerCertificationEntity.ensureBelongsTo(applicationId);

        return new ValidationResult(application, foundSellerCertificationEntity);
    }

    private SellerApplicationEntity getApplication(UUID id) {
        return entityReferenceResolver.getRequired(SellerApplicationEntity.class, id);
    }

}
