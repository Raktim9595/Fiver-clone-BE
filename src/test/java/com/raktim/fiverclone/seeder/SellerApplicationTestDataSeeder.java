package com.raktim.fiverclone.seeder;

import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.seeds.skills.SkillsRepo;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationStatusHistoryEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationRepo;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationStatusHistoryRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@TestComponent
@RequiredArgsConstructor
public class SellerApplicationTestDataSeeder {
    private final SellerApplicationRepo sellerApplicationRepo;
    private final SellerApplicationStatusHistoryRepo sellerApplicationStatusHistoryRepo;
    private final SkillsRepo skillsRepo;

    public SellerApplicationEntity addSellerApplication(UserEntity user) {
        List<SkillEntity> skills = skillsRepo.findAll();

        SellerApplicationEntity entity =
                SellerApplicationEntity
                        .builder()
                        .user(user)
                        .skills(Set.of(skills.getFirst(), skills.getLast()))
                        .submittedAt(Instant.now())
                        .build();

        return  sellerApplicationRepo.save(entity);
    }

    public SellerApplicationEntity addSellerApplication(
            UserEntity user,
            SellerApplicationEntity.SellerApplicationEntityBuilder entityBuilder
    ) {
        List<SkillEntity> skills = skillsRepo.findAll();

        SellerApplicationEntity entity =
                entityBuilder
                        .user(user)
                        .skills(Set.of(skills.getFirst(), skills.getLast()))
                        .submittedAt(Instant.now())
                        .build();

        return  sellerApplicationRepo.save(entity);
    }

    public SellerApplicationStatusHistoryEntity addSellerApplicationStatusHistory
            (
                    UserEntity user,
                    SellerApplicationEntity application
            ) {
        SellerApplicationStatusHistoryEntity entity =
                SellerApplicationStatusHistoryEntity
                        .builder()
                        .previousStatus(SellerApplicationStatus.DRAFT)
                        .newStatus(SellerApplicationStatus.APPROVED)
                        .changedBy(user)
                        .application(application)
                        .reason("Everything looks fine so approved")
                        .build();

        return sellerApplicationStatusHistoryRepo.save(entity);
    }

    public SellerApplicationEntity getApplication(UUID id) {
        return sellerApplicationRepo.findById(id).orElse(null);
    }
}

