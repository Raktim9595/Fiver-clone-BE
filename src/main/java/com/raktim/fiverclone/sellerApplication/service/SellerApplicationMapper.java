package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationStatusHistoryEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerEntity;
import com.raktim.fiverclone.user.utils.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerApplicationMapper {
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "changedBy", source = "changedBy.username")
    @Mapping(target = "currentStatus", source = "newStatus")
    SellerApplicationHistoryResponseDto
        toSellerApplicationHistoryResponseDto(SellerApplicationStatusHistoryEntity entity);
}