package com.raktim.fiverclone.role;

import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RoleServiceIntegrationTest {
    @Autowired
    private RoleService service;

    @Test
    public void testFindAll() {
        List<RoleEntity> roles = service.findAll();

        assertThat(roles)
                .hasSize(3);

        assertThat(roles)
                .extracting(RoleEntity::getName)
                .containsExactlyInAnyOrder(
                        UserRole.ADMIN,
                        UserRole.BUYER,
                        UserRole.SELLER
                );
    }
}
