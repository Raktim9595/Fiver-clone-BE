package com.raktim.fiverclone.seeds.experienceLevel;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceLevelService {
    private final ExperienceLevelRepo repo;

    private static final Logger log =  LoggerFactory.getLogger(ExperienceLevelService.class);

    public List<ExperienceLevelEntity> getExperienceLevels() {
        log.info("Fetching all the experience levels");
        return repo.findAll();
    }
}
