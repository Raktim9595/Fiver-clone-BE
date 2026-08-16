package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerEducationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerEducationRepo;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerEducationService {
    private final SellerEducationRepo repo;
    private final SellerApplicationService sellerApplicationService;
    private final SellerApplicationMapper mapper;

    private static final Logger log =  LoggerFactory.getLogger(SellerEducationService.class);

    public SellerEducationResponseDto create(
            UUID applicationId,
            SellerEducationRequestDto dto
    ) {
        log.info("Creating SellerEducationEntity {} of applicationId={}", dto, applicationId);

        SellerApplicationEntity sellerApplicationEntity =
                sellerApplicationService.findByIdOrThrow(applicationId);

        SellerEducationEntity newSellerEducationEntity =
                mapper.toSellerEducationEntity(dto, sellerApplicationEntity);

        repo.save(newSellerEducationEntity);
        log.info("Successfully saved seller education entity");

        return mapper.toSellerEducationResponseDto(newSellerEducationEntity);
    }
}
