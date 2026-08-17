package com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile;

import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileResponseDto;

import java.util.UUID;

public interface SellerPersonalProfileService {
    SellerPersonalProfileResponseDto create(UUID applicationId, SellerPersonalProfileRequestDto dto);

}
