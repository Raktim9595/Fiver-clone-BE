package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import com.raktim.fiverclone.sellerApplication.repo.OccupationRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OccupationService {
    private final OccupationRepo repo;

    private static final Logger log = LoggerFactory.getLogger(OccupationService.class);

    public OccupationEntity createOccupation(String occupationName) {
        log.info("Creating occupation with name {}", occupationName);

        OccupationEntity newOccupation = OccupationEntity
                .builder()
                .name(occupationName)
                .build();

        return repo.save(newOccupation);
    }

    public List<OccupationEntity> findAllOccupations() {
        log.info("Finding all occupations");
        return repo.findAll();
    }
}
