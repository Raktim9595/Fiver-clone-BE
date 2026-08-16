package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.sellerApplicationStatusHistory.SellerApplicationStatusHistoryService;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerApplicationHistoryIntegrationTest {
    @Autowired
    private SellerApplicationStatusHistoryService historyService;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    private UserEntity changedByUser;
    private UserEntity sameUser;
    private SellerApplicationEntity application;

    @BeforeEach
    public void setup() {
        sameUser = userTestDataSeeder.addUser();
        changedByUser = userTestDataSeeder.addUser();
        this.application = sellerApplicationTestDataSeeder.addSellerApplication(sameUser);
    }

    @Test
    @DisplayName("""
            When called createApplicationHistory,
            And there is no any error,
            Then it should create a history log for respective application and return proper data
            """)
    void shouldCreateApplicationHistory() {

        SellerApplicationHistoryRequestDto request =
                SellerApplicationHistoryRequestDto
                        .builder()
                        .reason("Something went wrong")
                        .changedBy(changedByUser.getId())
                        .previousStatus(SellerApplicationStatus.DRAFT)
                        .newStatus(SellerApplicationStatus.REJECTED)
                        .build();

        var response =
                historyService.createApplicationHistory(
                        application.getId(),
                        request
                );

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(SellerApplicationHistoryResponseDto.class);
        assertThat(response.changedBy()).isEqualTo(changedByUser.getUsername());
        assertThat(response.reason()).isEqualTo("Something went wrong");
        assertThat(response.currentStatus()).isEqualTo(SellerApplicationStatus.REJECTED);
    }

    @Test
    @DisplayName("""
            When called createApplicationHistory,
            And there is error of current applicant trying to modify the status,
            Then it should create a history log for respective application and return proper data
            """)
    void shouldFailCreateApplicationHistory() {

        SellerApplicationHistoryRequestDto request =
                SellerApplicationHistoryRequestDto
                        .builder()
                        .reason("Something went wrong")
                        .changedBy(sameUser.getId())
                        .previousStatus(SellerApplicationStatus.DRAFT)
                        .newStatus(SellerApplicationStatus.REJECTED)
                        .build();

        ExceptionTestUtil.assertBusinessException
                (
                        HttpStatus.FORBIDDEN,
                        "ACTION_NOT_ALLOWED",
                        "You are not allowed to perform this action since you are the one who submitted the application.",
                        () -> historyService.createApplicationHistory(application.getId(),request)
                );
    }
}
