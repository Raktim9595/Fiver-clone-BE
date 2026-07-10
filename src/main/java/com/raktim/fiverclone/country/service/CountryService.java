package com.raktim.fiverclone.country.service;

import com.raktim.fiverclone.country.model.CountryEntity;
import com.raktim.fiverclone.country.repo.CountryRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepo countryRepo;

    private static final Logger log =  LoggerFactory.getLogger(CountryService.class);

    public List<CountryEntity> findAll() {
        log.info("Getting all countries");
        return countryRepo.findAll();
    }
}
