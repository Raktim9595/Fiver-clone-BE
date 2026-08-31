package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerProfessionalProfileEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerProfessionalProfileRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerProfessionalProfileService {
    private final SellerProfessionalProfileRepo repo;
    private final SellerApplicationMapper mapper;
    private final EntityReferenceResolver entityReferenceResolver;

    private static final Logger log = LoggerFactory.getLogger(SellerProfessionalProfileService.class);

    @Transactional
    public SellerProfessionalProfileResponseDto createProfessionalProfile(
            UUID applicationId,
            SellerProfessionalProfileRequestDto dto
    ) {
        log.info("Creating personal profile for application {}", applicationId);

        SellerApplicationEntity application = entityReferenceResolver.getRequired(
                SellerApplicationEntity.class,
                applicationId
        );

        application.ensureEditableForProfessionalProfile();

        OccupationEntity occupation = getOccupation(dto.occupationId());

        SellerProfessionalProfileEntity sellerProfessionalProfileEntity =
                mapper.toSellerProfessionalProfileEntity(dto, application, occupation);

        application.setCompletionPercentage(100);
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        repo.save(sellerProfessionalProfileEntity);
        log.info("Created personal profile for application {}", applicationId);

        return mapper.toSellerProfessionalProfileResponseDto(sellerProfessionalProfileEntity);
    }

    @Transactional
    public SellerProfessionalProfileResponseDto update(
            UUID id,
            UUID applicationId,
            SellerProfessionalProfileRequestDto dto
    ) {
        log.info("Updating personal profile for application {}", id);
        SellerApplicationEntity foundApplication =
                entityReferenceResolver.getRequired(SellerApplicationEntity.class, applicationId);
        SellerProfessionalProfileEntity foundPersonalProfileEntity =
                entityReferenceResolver.getRequired(SellerProfessionalProfileEntity.class, id);

        // validation for the entities can be editable
        foundApplication.ensureEditableDuringReview();
        foundPersonalProfileEntity.ensureBelongsTo(applicationId);
        // validation steps end here

        OccupationEntity occupation = getOccupation(dto.occupationId());
        mapper.updateSellerProfessionalProfileFromDto(dto, occupation, foundPersonalProfileEntity);
        log.info("Successfully updated personal profile for application {}", id);
        return mapper.toSellerProfessionalProfileResponseDto(foundPersonalProfileEntity);
    }

    private OccupationEntity getOccupation(UUID id) {
        return entityReferenceResolver.getRequired(OccupationEntity.class, id);
    }
}
