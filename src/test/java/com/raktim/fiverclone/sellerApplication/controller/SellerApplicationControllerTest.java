package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.common.utils.JWTUtil;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.service.SellerCertificationService;
import com.raktim.fiverclone.sellerApplication.service.SellerEducationService;
import com.raktim.fiverclone.sellerApplication.service.SellerPortfolioService;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserPrincipal;
import com.raktim.fiverclone.user.service.CustomUserDetailService;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import com.raktim.fiverclone.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerApplicationController.class)
public class SellerApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SellerApplicationService sellerApplicationService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @MockitoBean
    private SellerEducationService sellerEducationService;

    @MockitoBean
    private SellerPortfolioService sellerPortfolioService;

    @MockitoBean
    private SellerCertificationService sellerCertificationService;

    // A plain @WebMvcTest does not register Spring Security's @AuthenticationPrincipal support,
    // so without this Spring would build an EMPTY UserPrincipal instead of using the logged-in one.
    // @EnableWebSecurity registers that support; the chain below keeps every request open so the
    // other tests in this class (which send no token) are unaffected.
    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                    .build();
        }
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application, And no issues
            Then it should start the application for the LOGGED IN user (taken from the token, not the body)
            """)
    public void testAddSeller() throws Exception  {
        UUID userId = UUID.randomUUID();
        UserEntity entity = UserTestDataFactory.validUserEntity().build();
        entity.setId(userId);
        UserPrincipal principal = new UserPrincipal(entity);

        mockMvc.perform(post("/api/seller-application")
                .with(authentication(new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()))))
                .andExpect(status().isCreated());

        verify(sellerApplicationService).startSellerApplication(userId);
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/seller-education,
            And there is no error,
            Then it should invoke the respective method and return proper response
            """)
    public void shouldExecuteAddSellerEducation() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerEducationRequestDto dto = SellerApplicationTestData
                .validSellerEducationRequestDto()
                .build();

        mockMvc.perform(post("/api/seller-application/{applicationId}/seller-education", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerEducationService, times(1)).create(applicationId, dto);
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/seller-portfolio,
            And there is no error,
            Then it should invoke the respective method and return proper response
            """)
    public void shouldExecuteAddSellerPortfolio() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerPortfolioRequestDto dto = SellerApplicationTestData.validSellerPortfolioRequestDto().build();

        mockMvc.perform(post("/api/seller-application/{id}/seller-portfolio", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerPortfolioService, times(1)).create(applicationId, dto);
    }

    @Test
    @DisplayName("""
            When called POST on /api/seller-application/{id}/seller-certification,
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteAddSellerCertification() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        SellerCertificationRequestDto dto = SellerApplicationTestData.validSellerCertificationRequestDto().build();

        mockMvc.perform(post("/api/seller-application/{id}/seller-certification", applicationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());

        verify(sellerCertificationService, times(1)).create(applicationId, dto);
    }

    @Test
    @DisplayName("""
            When called PUT on /api/seller-application/{id}/seller-education/{educationId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteUpdateSellerEducation() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID educationId = UUID.randomUUID();
        SellerEducationRequestDto dto = SellerApplicationTestData.validSellerEducationRequestDto().build();

        mockMvc.perform(put("/api/seller-application/{id}/seller-education/{educationId}",
                applicationId, educationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());

        verify(sellerEducationService, times(1)).update(
                educationId, applicationId, dto
        );
    }

    @Test
    @DisplayName("""
            When called PUT on /api/seller-application/{id}/seller-portfolio/{portfolioId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteUpdateSellerPortfolio() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID portfolioId = UUID.randomUUID();
        SellerPortfolioRequestDto dto = SellerApplicationTestData.validSellerPortfolioRequestDto().build();

        mockMvc.perform(put("/api/seller-application/{id}/seller-portfolio/{portfolioId}",
                applicationId, portfolioId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());

        verify(sellerPortfolioService, times(1)).update(
                portfolioId, applicationId, dto
        );
    }

    @Test
    @DisplayName("""
            When called PUT on /api/seller-application/{id}/seller-certification/{certificationId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteUpdateSellerCertification() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID certificationId = UUID.randomUUID();
        SellerCertificationRequestDto dto = SellerApplicationTestData.validSellerCertificationRequestDto().build();

        mockMvc.perform(put("/api/seller-application/{id}/seller-certification/{certificationId}",
                applicationId, certificationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());

        verify(sellerCertificationService, times(1)).update(
                certificationId, applicationId, dto
        );
    }

    @Test
    @DisplayName("""
            When called DELETE on /api/seller-application/{id}/seller-certification/{certificationId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteDeleteSellerCertification() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID certificationId = UUID.randomUUID();

        mockMvc.perform(delete("/api/seller-application/{id}/seller-certification/{certificationId}",
                applicationId, certificationId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(sellerCertificationService, times(1)).delete(certificationId, applicationId);
    }

    @Test
    @DisplayName("""
            When called DELETE on /api/seller-application/{id}/seller-education/{educationId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteDeleteSellerEducation() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID educationId = UUID.randomUUID();

        mockMvc.perform(delete("/api/seller-application/{id}/seller-education/{educationId}",
                applicationId, educationId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(sellerEducationService, times(1)).delete(educationId, applicationId);
    }

    @Test
    @DisplayName("""
            When called DELETE on /api/seller-application/{id}/seller-portfolio/{portfolioId},
            Then it should invoke proper methods and return proper response
            """)
    public void shouldExecuteDeleteSellerPortfolio() throws Exception  {
        UUID applicationId = UUID.randomUUID();
        UUID portfolioId = UUID.randomUUID();

        mockMvc.perform(delete("/api/seller-application/{id}/seller-portfolio/{educationId}",
                applicationId, portfolioId)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(sellerPortfolioService, times(1)).delete(portfolioId, applicationId);
    }

    @Test
    @DisplayName("""
            When called GET on /api/seller-application/{id},
            And there is no error,
            Then it should invoke proper method in the service and return proper entity
            """)
    public void shouldExecuteGetSellerApplication() throws Exception  {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/api/seller-application/{id}", id)).andExpect(status().isOk());

        verify(sellerApplicationService, times(1)).findByIdOrThrow(id);
    }
}
