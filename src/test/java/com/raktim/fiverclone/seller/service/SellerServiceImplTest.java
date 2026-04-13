package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelDto;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelEntity;
import com.raktim.fiverclone.seeds.skills.SkillDto;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.dto.SellerResponseDto;
import com.raktim.fiverclone.seller.model.SellerEntity;
import com.raktim.fiverclone.seller.repo.SellerRepo;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.utils.UserMapper;
import com.raktim.fiverclone.user.utils.UserMapperImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceImplTest {
    @Mock
    private SellerRepo sellerRepo;

    @Mock
    private EntityManager entityManager;

    private SellerServiceImpl sellerService;

    @BeforeEach
    public void setUp() {
        UserMapper userMapper = new UserMapperImpl();
        SellerMapper sellerMapper = new SellerMapperImpl();

        ReflectionTestUtils.setField(sellerMapper, "userMapper", userMapper);
        sellerService = new SellerServiceImpl(sellerRepo, entityManager, sellerMapper);
    }

    @Test
    @DisplayName("Done")
    public void testAddSeller() {
        // mocks
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID skillId1 = UUID.randomUUID();
        UUID skillId2 = UUID.randomUUID();
        UUID experienceId = UUID.randomUUID();

        SellerDto sellerDto = SellerDto.builder()
                .userId(userId)
                .description("Java Developer")
                .experienceId(experienceId)
                .skills(Set.of(skillId1, skillId2))
                .build();

        UserEntity userRef = new UserEntity();
        userRef.setId(userId);

        ExperienceLevelEntity experienceRef = new ExperienceLevelEntity();
        experienceRef.setId(experienceId);

        SkillEntity skill1 = new SkillEntity();
        skill1.setId(skillId1);

        SkillEntity skill2 = new SkillEntity();
        skill2.setId(skillId2);

        // building the entity
        SellerEntity savedEntity = SellerEntity.builder()
                .baseUser(userRef)
                .experience(experienceRef)
                .skills(Set.of(skill1, skill2))
                .description("Java backend developer")
                .totalEarnings(BigDecimal.ZERO)
                .totalOrders(0)
                .build();

        savedEntity.setId(id);

        // mocking the returns of the dependencies
        when(entityManager.getReference(UserEntity.class, userId)).thenReturn(
                UserTestDataFactory.validUserEntity().build()
        );
        when(entityManager.getReference(ExperienceLevelEntity.class, experienceId)).thenReturn(experienceRef);
        when(entityManager.getReference(SkillEntity.class, skillId1)).thenReturn(skill1);
        when(entityManager.getReference(SkillEntity.class, skillId2)).thenReturn(skill2);

        when(sellerRepo.save(any(SellerEntity.class))).thenReturn(savedEntity);
        SellerResponseDto response = sellerService.addSeller(sellerDto);

        assertNotNull(response);
        assertInstanceOf(SellerResponseDto.class, response);
        assertInstanceOf(UserListResponseDto.class, response.baseUser());
        assertInstanceOf(SkillDto.class, response.skills().getFirst());
        assertInstanceOf(ExperienceLevelDto.class, response.experience());
    }

}
