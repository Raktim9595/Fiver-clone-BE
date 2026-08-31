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

    private static final Logger log =  LoggerFactory.getLogger(SellerCertificationService.class);

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
        SellerApplicationEntity application = getApplication(applicationId);
        SellerCertificationEntity foundSellerCertificationEntity =
                entityReferenceResolver.getRequired(SellerCertificationEntity.class, id);

        // validations
        application.ensureEditableDuringReview();
        foundSellerCertificationEntity.ensureBelongsTo(applicationId);

        mapper.updateSellerCertificationFromDto(dto, foundSellerCertificationEntity);
        log.info("Successfully updated Seller Certification with applicationId {}", applicationId);

        return mapper.toSellerCertificationResponseDto(foundSellerCertificationEntity);
    }

    private SellerApplicationEntity getApplication(UUID id) {
        return entityReferenceResolver.getRequired(SellerApplicationEntity.class, id);
    }
}
