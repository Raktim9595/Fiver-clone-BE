package com.raktim.fiverclone.timezone.service;

import com.raktim.fiverclone.timezone.model.TimeZoneEntity;
import com.raktim.fiverclone.timezone.repo.TimezoneRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimezoneService {
    private final TimezoneRepo timezoneRepo;

    private final Logger logger = LoggerFactory.getLogger(TimezoneService.class);

    public List<TimeZoneEntity> findAll() {
        logger.info("Fetching all time zones");
        return timezoneRepo.findAll();
    }
}
