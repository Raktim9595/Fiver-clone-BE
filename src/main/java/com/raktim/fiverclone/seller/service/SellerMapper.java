package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.seller.dto.SellerResponseDto;
import com.raktim.fiverclone.seller.model.SellerEntity;
import com.raktim.fiverclone.user.utils.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SellerMapper {
    SellerResponseDto toResponseDto(SellerEntity entity);
}