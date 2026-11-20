package com.raktim.fiverclone.config;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.role.repo.RoleRepo;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserPrincipal;
import com.raktim.fiverclone.user.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end check of the whole security chain: real JWT -> AuthTokenFilter -> database user
 * (with role) -> ROLE_ authority -> @PreAuthorize("hasRole('ADMIN')").
 * The endpoint below exists only in tests; it stands in for the future admin review endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminGuardIntegrationTest {
    private static final String ADMIN_URL = "/api/test-admin/ping";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @TestConfiguration
    static class ProbeConfig {
        @Bean
        AdminProbeController adminProbeController() {
            return new AdminProbeController();
        }
    }

    @RestController
    static class AdminProbeController {
        @GetMapping(ADMIN_URL)
        @PreAuthorize("hasRole('ADMIN')")
        public String ping() {
            return "pong";
        }
    }

    @Test
    @DisplayName("Given no token, When calling an ADMIN-only endpoint, Then 401")
    void anonymous_isRejectedWith401() throws Exception {
        mockMvc.perform(get(ADMIN_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given a BUYER token, When calling an ADMIN-only endpoint, Then 403")
    void buyer_isForbidden() throws Exception {
        String token = tokenFor(UserRole.BUYER, "buyer-user", "buyer@gmail.com");

        mockMvc.perform(get(ADMIN_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given a SELLER token, When calling an ADMIN-only endpoint, Then 403")
    void seller_isForbidden() throws Exception {
        String token = tokenFor(UserRole.SELLER, "seller-user", "seller@gmail.com");

        mockMvc.perform(get(ADMIN_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given an ADMIN token, When calling an ADMIN-only endpoint, Then 200")
    void admin_isAllowed() throws Exception {
        String token = tokenFor(UserRole.ADMIN, "admin-user", "admin@gmail.com");

        mockMvc.perform(get(ADMIN_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String tokenFor(UserRole roleName, String username, String email) {
        RoleEntity role = roleRepo.findAll().stream()
                .filter(r -> r.getName() == roleName)
                .findFirst()
                .orElseThrow();

        UserEntity user = userRepo.save(
                UserTestDataFactory.validUserEntity()
                        .username(username)
                        .email(email)
                        .role(role)
                        .build()
        );

        return jwtUtil.generateToken(new UserPrincipal(user));
    }
}
