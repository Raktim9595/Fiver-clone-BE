package com.raktim.fiverclone.sellerApplication.service.sellerApplication;

import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;

import java.util.UUID;

public interface SellerApplicationService {
    SellerApplicationEntity startSellerApplication(UUID userId);
    SellerApplicationEntity findByIdOrThrow(UUID id);
    SellerApplicationEntity completeSellerApplication(UUID id, UUID userId);
}
