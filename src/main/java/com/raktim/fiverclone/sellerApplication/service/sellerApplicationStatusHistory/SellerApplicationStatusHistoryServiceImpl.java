package com.raktim.fiverclone.sellerApplication.service.sellerApplicationStatusHistory;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationStatusHistoryEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationStatusHistoryRepo;
import com.raktim.fiverclone.sellerApplication.service.SellerApplicationMapper;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerApplicationStatusHistoryServiceImpl
implements SellerApplicationStatusHistoryService{
    private final SellerApplicationStatusHistoryRepo repo;
    private final UserService userService;
    private final SellerApplicationService applicationService;
    private final SellerApplicationMapper mapper;

    private final static Logger log =
            LoggerFactory.getLogger(SellerApplicationStatusHistoryServiceImpl.class);

    @Override
    public SellerApplicationHistoryResponseDto createApplicationHistory
            (
                UUID applicationId,
                SellerApplicationHistoryRequestDto dto
            )
    {
        log.info(
                "Creating application history for application id {} and history details {}",
                applicationId, dto
        );

        SellerApplicationStatusHistoryEntity newEntity =
                this.buildEntity(applicationId, dto);

        SellerApplicationStatusHistoryEntity savedEntity = repo.save(newEntity);
        log.info("Created application history for application id {}", applicationId);
        return mapper.toSellerApplicationHistoryResponseDto(savedEntity);
    }

    private SellerApplicationStatusHistoryEntity buildEntity(
            UUID applicationId,
            @NonNull SellerApplicationHistoryRequestDto dto
    ) {
        SellerApplicationEntity application =
                applicationService.findByIdOrThrow(applicationId);

        UserEntity user = userService.findUserByIdOrThrow(dto.changedBy());

        // This implies user who submitted the application is trying to change the status of application.
        // In this case we should throw an exception
        if (dto.changedBy().equals(application.getUser().getId())) {
            throw new BusinessException(
                    HttpStatus.FORBIDDEN,
                    "ACTION_NOT_ALLOWED",
                    "You are not allowed to perform this action since you are the one who submitted the application."
            );
        }

        return SellerApplicationStatusHistoryEntity
                        .builder()
                        .application(application)
                        .previousStatus(dto.previousStatus())
                        .newStatus(dto.newStatus())
                        .changedBy(user)
                        .reason(dto.reason())
                        .build();
    }
}
