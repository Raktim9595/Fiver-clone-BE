package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.sellerApplication.dto.*;
import com.raktim.fiverclone.sellerApplication.model.*;
import org.mapstruct.*;

import java.util.Set;

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
    @Mapping(target = "occupation", source = "occupation")
    SellerProfessionalProfileEntity toSellerProfessionalProfileEntity(
            SellerProfessionalProfileRequestDto dto,
            SellerApplicationEntity applicationEntity,
            OccupationEntity occupation
    );

    @Mapping(target = "application", source = "applicationEntity")
    SellerCertificationEntity toSellerCertificationEntity(
            SellerCertificationRequestDto dto,
            SellerApplicationEntity applicationEntity
    );

    @Mapping(target = "application", source = "applicationEntity")
    @Mapping(target = "languages", source = "languages")
    SellerPersonalProfileEntity toSellerPersonalProfileEntity(
            SellerPersonalProfileRequestDto dto,
            SellerApplicationEntity applicationEntity,
            Set<LanguageEntity> languages
    );

    @Mapping(target = "applicationId", source = "application.id")
    SellerEducationResponseDto toSellerEducationResponseDto(
            SellerEducationEntity sellerEducationEntity
    );

    @Mapping(target = "applicationId", source = "application.id")
    SellerPortfolioResponseDto toSellerPortfolioResponseDto(
            SellerPortfolioEntity sellerPortfolioEntity
    );

    @Mapping(target = "applicationId", source = "application.id")
    SellerCertificationResponseDto toSellerCertificationResponseDto(
            SellerCertificationEntity sellerCertificationEntity
    );

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "occupation", source = "occupation.name")
    SellerProfessionalProfileResponseDto toSellerProfessionalProfileResponseDto(
            SellerProfessionalProfileEntity sellerProfessionalProfileEntity
    );

    SellerPersonalProfileResponseDto toSellerPersonalProfileResponseDto(
            SellerPersonalProfileEntity entity
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "languages", source = "languages")
    void updateSellerPersonalProfileFromDto(
            SellerPersonalProfileRequestDto dto,
            Set<LanguageEntity> languages,
            @MappingTarget SellerPersonalProfileEntity entity
    );

    default String mapLanguageToName(LanguageEntity language) {
        return language.getLanguage();
    }
}