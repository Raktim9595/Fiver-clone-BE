package com.raktim.fiverclone.seeder;

import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.language.repo.LanguageRepo;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

@TestComponent
@RequiredArgsConstructor
public class UserTestDataSeeder {
    private final UserRepo userRepo;
    private final LanguageRepo languageRepo;

    public UserEntity addUser() {
        UserEntity user = UserTestDataFactory.validUserEntity().build();
        return userRepo.save(user);
    }

    public List<LanguageEntity> findAllLanguages() {
        return languageRepo.findAll();
    }
}
