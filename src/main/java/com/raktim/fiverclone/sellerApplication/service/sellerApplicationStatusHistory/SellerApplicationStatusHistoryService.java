package com.raktim.fiverclone.sellerApplication.service.sellerApplicationStatusHistory;

import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;

import java.util.UUID;

public interface SellerApplicationStatusHistoryService {
    SellerApplicationHistoryResponseDto createApplicationHistory
            (
                    UUID applicationId,
                    SellerApplicationHistoryRequestDto sellerApplicationHistoryRequestDto
            );
}
