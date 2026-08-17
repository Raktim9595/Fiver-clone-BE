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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerProfessionalProfileService {
    private final SellerProfessionalProfileRepo repo;
    private final SellerApplicationMapper mapper;
    private final EntityReferenceResolver entityReferenceResolver;

    private static final Logger log = LoggerFactory.getLogger(SellerProfessionalProfileService.class);

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

        OccupationEntity occupation = entityReferenceResolver.getRequired(
                OccupationEntity.class,
                dto.occupationId()
        );

        SellerProfessionalProfileEntity sellerProfessionalProfileEntity =
                mapper.toSellerProfessionalProfileEntity(dto, application, occupation);

        application.setCompletionPercentage(100);
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        repo.save(sellerProfessionalProfileEntity);
        log.info("Created personal profile for application {}", applicationId);

        return mapper.toSellerProfessionalProfileResponseDto(sellerProfessionalProfileEntity);
    }
}
