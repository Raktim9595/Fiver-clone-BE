package com.raktim.fiverclone.sellerApplication.service.sellerApplication;

import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;

import java.util.UUID;

public interface SellerApplicationService {
    SellerApplicationEntity startSellerApplication
            (StartSellerApplicationRequestDto startSellerApplicationRequestDto);
    SellerApplicationEntity findByIdOrThrow(UUID id);
}
