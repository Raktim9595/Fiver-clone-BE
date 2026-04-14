package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelEntity;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.dto.SellerResponseDto;
import com.raktim.fiverclone.seller.model.SellerEntity;
import com.raktim.fiverclone.seller.repo.SellerRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserRole;
import com.raktim.fiverclone.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepo sellerRepo;
    private final EntityManager entityManager;
    private final SellerMapper sellerMapper;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    @Transactional
    public SellerResponseDto addSeller(SellerDto sellerDto) {
        log.info("Adding seller {}", sellerDto);

        SellerEntity sellerEntity = buildEntity(sellerDto);
        SellerEntity result = sellerRepo.save(sellerEntity);

        return sellerMapper.toResponseDto(result);
    }


    private SellerEntity buildEntity(SellerDto sellerDto) {
        UserEntity user = userService.findUserByIdOrThrow(sellerDto.userId());
        user.setRole(UserRole.SELLER);
        ExperienceLevelEntity experienceRef =
                entityManager.getReference(ExperienceLevelEntity.class, sellerDto.experienceId());

        Set<SkillEntity> skills = sellerDto.skills()
                .stream()
                .map(id -> entityManager.getReference(SkillEntity.class, id))
                .collect(Collectors.toSet());

        return SellerEntity.builder()
                .baseUser(user)
                .experience(experienceRef)
                .skills(skills)
                .description(sellerDto.description())
                .build();
    }
}
