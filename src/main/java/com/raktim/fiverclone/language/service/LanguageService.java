package com.raktim.fiverclone.language.service;

import com.raktim.fiverclone.language.dto.LanguageDto;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.language.repo.LanguageRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepo languageRepo;

    private static final Logger log = LoggerFactory.getLogger(LanguageService.class);

    public List<LanguageDto>  findAll() {
        log.info("Finding all languages");

        List<LanguageEntity> result = languageRepo.findAll();
        return result.stream()
                .map(lang -> new LanguageDto(
                        lang.getId(),
                        lang.getCode(),
                        lang.getLanguage()
                )).toList();
    }
}
