package com.raktim.fiverclone.timezone;

import com.raktim.fiverclone.timezone.model.TimeZoneEntity;
import com.raktim.fiverclone.timezone.repo.TimezoneRepo;
import com.raktim.fiverclone.timezone.service.TimezoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TimezoneServiceIntegrationTest {
    @Autowired
    private TimezoneService timezoneService;

    @Autowired
    private TimezoneRepo timezoneRepo;

    @Test
    @DisplayName("""
            Given findAll, When called
            Then it should return list of all the entities available in the db
            """)
    void findAll_shouldReturnAllTimezones() {
        timezoneRepo.deleteAll();

        int numberOfTimezones = timezoneService.findAll().size();
        assertThat(numberOfTimezones).isEqualTo(0);

        TimeZoneEntity utc = TimeZoneEntity.builder()
                .id(UUID.randomUUID())
                .code("UTC")
                .build();

        TimeZoneEntity sydney = TimeZoneEntity.builder()
                .id(UUID.randomUUID())
                .code("Australia/Sydney")
                .build();

        timezoneRepo.saveAll(List.of(utc, sydney));

        List<TimeZoneEntity> result = timezoneService.findAll();

        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(TimeZoneEntity::getCode)
                .containsExactlyInAnyOrder("UTC", "Australia/Sydney");

        assertThat(result)
                .extracting(TimeZoneEntity::getId)
                .doesNotContainNull();
    }
}
