package com.raktim.fiverclone.sellerApplication.service.sellerApplication;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    @Override
    public SellerApplicationEntity findByIdOrThrow(UUID id) {
        log.info("Fetching the application with id {} from the repository", id);
        return repo.findById(id).orElseThrow(
                        () ->    new BusinessException(
                                HttpStatus.NOT_FOUND,
                                "APPLICATION_NOT_FOUND",
                                "Application with id " + id + " was not found."
                        )
                );
    }
}
