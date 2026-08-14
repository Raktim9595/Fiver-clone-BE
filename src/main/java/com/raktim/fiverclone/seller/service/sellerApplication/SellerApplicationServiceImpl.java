package com.raktim.fiverclone.seller.service.sellerApplication;

import com.raktim.fiverclone.seller.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.seller.model.SellerApplicationEntity;
import com.raktim.fiverclone.seller.repo.SellerApplicationRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerApplicationServiceImpl implements SellerApplicationService {
    private final SellerApplicationRepo repo;
    private final UserService userService;

    private final static Logger log = LoggerFactory.getLogger(SellerApplicationServiceImpl.class);

    @Override
    @Transactional
    public SellerApplicationEntity startSellerApplication
            (StartSellerApplicationRequestDto startSellerApplicationRequestDto) {
        log.info("User {} has submitted the request to become the seller", startSellerApplicationRequestDto);
        UserEntity user = userService.findUserByIdOrThrow(startSellerApplicationRequestDto.userId());
        SellerApplicationEntity newEntity = SellerApplicationEntity.builder()
                .user(user)
                .build();
        repo.save(newEntity);

        log.info("Successfully saved the request of user {} to become the seller", startSellerApplicationRequestDto);

        return newEntity;
    }
}
