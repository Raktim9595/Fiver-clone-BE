package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerApplicationMapper {
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "changedBy", source = "changedBy.username")
    @Mapping(target = "currentStatus", source = "newStatus")
    SellerApplicationHistoryResponseDto
        toSellerApplicationHistoryResponseDto(SellerApplicationStatusHistoryEntity entity);

    @Mapping(target = "application", source = "applicationEntity")
    SellerEducationEntity toSellerEducationEntity(
            SellerEducationRequestDto dto,
            SellerApplicationEntity applicationEntity
    );

    @Mapping(target = "application", source = "applicationEntity")
    SellerPortfolioEntity toSellerPortfolioEntity(
            SellerPortfolioRequestDto dto,
            SellerApplicationEntity applicationEntity
    );

    @Mapping(target = "application", source = "applicationEntity")
    SellerCertificationEntity toSellerCertificationEntity(
            SellerCertificationRequestDto dto,
            SellerApplicationEntity applicationEntity
    );
}