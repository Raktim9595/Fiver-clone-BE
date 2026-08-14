package com.raktim.fiverclone.seller.service.sellerApplication;

import com.raktim.fiverclone.seller.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.seller.model.SellerApplicationEntity;

public interface SellerApplicationService {
    SellerApplicationEntity startSellerApplication(StartSellerApplicationRequestDto startSellerApplicationRequestDto);
}
