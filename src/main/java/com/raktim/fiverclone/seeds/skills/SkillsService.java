package com.raktim.fiverclone.seeds.skills;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillsService {
    private final SkillsRepo repo;

    private static final Logger log = LoggerFactory.getLogger(SkillsService.class);

    public List<SkillEntity> findAll() {
        log.info("Fetching all the skills available");
        return repo.findAll();
    }
}
