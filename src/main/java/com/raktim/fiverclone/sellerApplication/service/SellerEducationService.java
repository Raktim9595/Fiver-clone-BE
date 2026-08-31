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

        SellerApplicationEntity foundApplication = getApplication(applicationId);
        SellerEducationEntity foundSellerEducationEntity =
                entityResolver.getRequired(SellerEducationEntity.class, id);

        // update validations
        foundApplication.ensureEditableDuringReview();
        foundSellerEducationEntity.ensureBelongsTo(applicationId);
        // validation ended

        mapper.updateSellerEducationFromDto(dto, foundSellerEducationEntity);
        log.info("Successfully updated seller education entity for applicationId={}", applicationId);
        return mapper.toSellerEducationResponseDto(foundSellerEducationEntity);
    }

    private SellerApplicationEntity getApplication(UUID applicationId) {
        return entityResolver.getRequired(SellerApplicationEntity.class, applicationId);
    }
}
