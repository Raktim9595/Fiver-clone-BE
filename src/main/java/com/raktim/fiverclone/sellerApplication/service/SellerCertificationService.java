package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerCertificationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerCertificationRepo;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerCertificationService {
    private final SellerCertificationRepo repo;
    private final SellerApplicationService sellerApplicationService;
    private final SellerApplicationMapper mapper;

    private static final Logger log =  LoggerFactory.getLogger(SellerCertificationService.class);

    public SellerCertificationEntity create(UUID applicationId, SellerCertificationRequestDto dto) {
        log.info("Creating Seller Certification {} with applicationId {}", dto, applicationId);

        SellerApplicationEntity application = sellerApplicationService.findByIdOrThrow(applicationId);

        SellerCertificationEntity newEntity =
                mapper.toSellerCertificationEntity(dto, application);

        SellerCertificationEntity result = repo.save(newEntity);

        log.info("Created Seller Certification {} with applicationId {}", dto, applicationId);

        return result;
    }
}
