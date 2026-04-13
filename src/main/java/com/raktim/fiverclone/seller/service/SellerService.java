package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.dto.SellerResponseDto;

public interface SellerService {
    SellerResponseDto addSeller(SellerDto sellerDto);
}
