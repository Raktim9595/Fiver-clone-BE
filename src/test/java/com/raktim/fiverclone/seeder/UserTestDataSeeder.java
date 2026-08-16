package com.raktim.fiverclone.seeder;

import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class UserTestDataSeeder {
    private final UserRepo userRepo;

    public UserEntity addUser() {
        UserEntity user = UserTestDataFactory.validUserEntity().build();
        return userRepo.save(user);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }
}
