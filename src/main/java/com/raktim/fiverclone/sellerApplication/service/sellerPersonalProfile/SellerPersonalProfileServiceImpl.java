package com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerPersonalProfileEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerPersonalProfileRepo;
import com.raktim.fiverclone.sellerApplication.service.SellerApplicationMapper;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerPersonalProfileServiceImpl implements SellerPersonalProfileService {
    private final SellerPersonalProfileRepo repo;
    private final SellerApplicationService sellerApplicationService;
    private final EntityReferenceResolver entityReferenceResolver;
    private final SellerApplicationMapper mapper;

    private static final Logger log =  LoggerFactory.getLogger(SellerPersonalProfileServiceImpl.class);

    @Override
    @Transactional
    public SellerPersonalProfileResponseDto create(UUID applicationId, SellerPersonalProfileRequestDto dto) {
        log.info("Creating SellerPersonalProfile for {} and applicationId {}", dto, applicationId);

        SellerApplicationEntity application = sellerApplicationService.findByIdOrThrow(applicationId);

        application.setCurrentStep(SellerOnboardingSteps.PERSONAL_PROFILE);
        application.setCompletionPercentage(50);

        Set<LanguageEntity> languages = dto.languages()
                .stream()
                .map(id -> entityReferenceResolver.getReference(LanguageEntity.class, id))
                .collect(Collectors.toSet());

        SellerPersonalProfileEntity sellerPersonalProfileEntity = mapper.toSellerPersonalProfileEntity(
                dto,
                application,
                languages
        );

        SellerPersonalProfileEntity sellerPersonalProfileEntitySaved = repo.save(sellerPersonalProfileEntity);
        log.info("Successfully saved the personal info for seller {}", applicationId);

        return mapper.toSellerPersonalProfileResponseDto(sellerPersonalProfileEntitySaved);
    }
}
