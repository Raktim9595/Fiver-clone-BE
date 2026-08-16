package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerPortfolioEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerPortfolioRepo;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerPortfolioService {
    private final SellerPortfolioRepo repo;
    private final SellerApplicationService sellerApplicationService;
    private final SellerApplicationMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(SellerPortfolioService.class);

    public SellerPortfolioEntity create(UUID applicationId, SellerPortfolioRequestDto dto) {
        log.info("Creating seller portfolio details {} for application {}", dto, applicationId);

        var application = sellerApplicationService.findByIdOrThrow(applicationId);

        SellerPortfolioEntity newSellerPortfolio =
                mapper.toSellerPortfolioEntity(dto, application);

        var result = repo.save(newSellerPortfolio);

        log.info("Created seller portfolio details {} for application {}", dto, applicationId);
        return result;
    }
}
