package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelDto;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelEntity;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.dto.SellerResponseDto;
import com.raktim.fiverclone.seller.model.SellerEntity;
import com.raktim.fiverclone.seller.repo.SellerRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepo sellerRepo;
    private final EntityManager entityManager;
    private final SellerMapper sellerMapper;

    private static final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    public SellerResponseDto addSeller(SellerDto sellerDto) {
        log.info("Adding seller {}", sellerDto);

        SellerEntity sellerEntity = buildEntity(sellerDto);
        SellerEntity result = sellerRepo.save(sellerEntity);

        return sellerMapper.toResponseDto(result);
    }



    private SellerEntity buildEntity(SellerDto sellerDto) {
        UserEntity userRef = entityManager.getReference(UserEntity.class, sellerDto.userId());
        ExperienceLevelEntity experienceRef =
                entityManager.getReference(ExperienceLevelEntity.class, sellerDto.experienceId());

        Set<SkillEntity> skills = sellerDto.skills()
                .stream()
                .map(id -> entityManager.getReference(SkillEntity.class, id))
                .collect(Collectors.toSet());

        return SellerEntity.builder()
                .baseUser(userRef)
                .experience(experienceRef)
                .skills(skills)
                .description(sellerDto.description())
                .build();
    }
}
