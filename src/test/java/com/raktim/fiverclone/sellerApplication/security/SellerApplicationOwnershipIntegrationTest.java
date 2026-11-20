package com.raktim.fiverclone.sellerApplication.security;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.role.repo.RoleRepo;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserPrincipal;
import com.raktim.fiverclone.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Runs real HTTP requests with real JWTs through the whole security chain and checks WHO may touch
 * a seller application: the owner, another user, an admin, or nobody (no token).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerApplicationOwnershipIntegrationTest {
    private static final String BASE = "/api/seller-application";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private RoleRepo roleRepo;
    @Autowired private UserTestDataSeeder userSeeder;
    @Autowired private SellerApplicationTestDataSeeder applicationSeeder;

    private UserEntity owner;
    private UserEntity stranger;
    private UserEntity admin;
    private SellerApplicationEntity application;

    @BeforeEach
    void setup() {
        owner = addUser("owner-user", "owner@gmail.com", UserRole.SELLER);
        stranger = addUser("stranger-user", "stranger@gmail.com", UserRole.SELLER);
        admin = addUser("admin-user", "admin@gmail.com", UserRole.ADMIN);
        application = applicationSeeder.addSellerApplication(owner);
    }

    // ---------- reading (owner or admin) ----------

    @Test
    @DisplayName("GET application: the owner gets 200")
    void get_owner_ok() throws Exception {
        mockMvc.perform(as(owner, get(BASE + "/" + application.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET application: an admin gets 200 (reviewers must be able to read)")
    void get_admin_ok() throws Exception {
        mockMvc.perform(as(admin, get(BASE + "/" + application.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET application: another user gets 403")
    void get_stranger_forbidden() throws Exception {
        mockMvc.perform(as(stranger, get(BASE + "/" + application.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET application: no token gets 401")
    void get_anonymous_unauthorized() throws Exception {
        mockMvc.perform(get(BASE + "/" + application.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET application: an id that does not exist still gets the normal 404")
    void get_unknownId_notFound() throws Exception {
        mockMvc.perform(as(owner, get(BASE + "/" + UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    // ---------- changing (owner only) ----------

    @Test
    @DisplayName("POST education: the owner gets 201")
    void addEducation_owner_created() throws Exception {
        mockMvc.perform(as(owner, post(BASE + "/" + application.getId() + "/seller-education")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(educationJson())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST education: another user gets 403")
    void addEducation_stranger_forbidden() throws Exception {
        mockMvc.perform(as(stranger, post(BASE + "/" + application.getId() + "/seller-education")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(educationJson())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST education: an admin gets 403 (admins can read applications but not change them)")
    void addEducation_admin_forbidden() throws Exception {
        mockMvc.perform(as(admin, post(BASE + "/" + application.getId() + "/seller-education")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(educationJson())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PATCH submit: another user gets 403 and the owner gets 200")
    void submit_onlyOwner() throws Exception {
        mockMvc.perform(as(stranger, patch(BASE + "/" + application.getId() + "/submit")))
                .andExpect(status().isForbidden());

        mockMvc.perform(as(owner, patch(BASE + "/" + application.getId() + "/submit")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE certification: another user gets 403 before anything is looked up")
    void deleteCertification_stranger_forbidden() throws Exception {
        mockMvc.perform(as(stranger, delete(
                        BASE + "/" + application.getId() + "/seller-certification/" + UUID.randomUUID())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT professional profile (path variable was renamed to {id}): another user gets 403")
    void updateProfessionalProfile_stranger_forbidden() throws Exception {
        mockMvc.perform(as(stranger, put(
                        BASE + "/" + application.getId() + "/professional-profile/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        // must be a VALID body: Spring validates the body before the security check runs
                        .content(objectMapper.writeValueAsString(
                                SellerApplicationTestData.validSellerProfessionalProfileRequestDto().build()))))
                .andExpect(status().isForbidden());
    }

    // ---------- status history (admin only) ----------

    @Test
    @DisplayName("POST status-history: the owner gets 403, an admin gets 201")
    void statusHistory_adminOnly() throws Exception {
        String body = objectMapper.writeValueAsString(
                SellerApplicationHistoryRequestDto.builder()
                        .changedBy(admin.getId())
                        .previousStatus(SellerApplicationStatus.SUBMITTED)
                        .newStatus(SellerApplicationStatus.UNDER_REVIEW)
                        .reason("Review started")
                        .build());

        mockMvc.perform(as(owner, post(BASE + "/" + application.getId() + "/status-history")
                        .contentType(MediaType.APPLICATION_JSON).content(body)))
                .andExpect(status().isForbidden());

        mockMvc.perform(as(admin, post(BASE + "/" + application.getId() + "/status-history")
                        .contentType(MediaType.APPLICATION_JSON).content(body)))
                .andExpect(status().isCreated());
    }

    // ---------- starting an application uses the logged in user ----------

    @Test
    @DisplayName("POST start application: it is created for the logged in user, no body needed")
    void start_usesLoggedInUser() throws Exception {
        UserEntity fresh = addUser("fresh-user", "fresh@gmail.com", UserRole.BUYER);

        mockMvc.perform(as(fresh, post(BASE)))
                .andExpect(status().isCreated());
    }

    // ---------- helpers ----------

    private UserEntity addUser(String username, String email, UserRole roleName) {
        RoleEntity role = roleRepo.findAll().stream()
                .filter(r -> r.getName() == roleName)
                .findFirst()
                .orElseThrow();

        return userSeeder.addUser(
                UserTestDataFactory.validUserEntity().username(username).email(email).role(role));
    }

    private MockHttpServletRequestBuilder as(UserEntity user, MockHttpServletRequestBuilder request) {
        return request.header("Authorization", "Bearer " + jwtUtil.generateToken(new UserPrincipal(user)));
    }

    private String educationJson() {
        return objectMapper.writeValueAsString(
                SellerApplicationTestData.validSellerEducationRequestDto().build());
    }
}
