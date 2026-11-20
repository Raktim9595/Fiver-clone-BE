package com.raktim.fiverclone.user.service;

import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserPrincipalIntegrationTest {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("""
            Given a saved user with the SELLER role,
            When the user details are loaded by username,
            Then the authorities contain ROLE_SELLER
            """)
    void loadUserByUsername_shouldExposeRoleAsAuthority() {
        userRepo.save(UserTestDataFactory.validUserEntity().build());
        entityManager.flush();
        entityManager.clear();

        UserDetails details = customUserDetailService.loadUserByUsername("Raktim");

        assertThat(details.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_SELLER");
    }

    @Test
    @DisplayName("""
            Given a saved user,
            When the user is fetched by username from a clean persistence context,
            Then the role is already loaded (no lazy load needed later in the security filter)
            """)
    void findByUsername_shouldFetchRoleEagerly() {
        userRepo.save(UserTestDataFactory.validUserEntity().build());
        entityManager.flush();
        entityManager.clear(); // forget everything so the next query really hits the database

        UserEntity found = userRepo.findByUsername("Raktim");

        boolean roleLoaded = entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(found, "role");

        assertThat(roleLoaded).isTrue();
    }
}
