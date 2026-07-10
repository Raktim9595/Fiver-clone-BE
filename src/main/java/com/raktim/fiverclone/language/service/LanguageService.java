package com.raktim.fiverclone.language.service;

import com.raktim.fiverclone.language.dto.LanguageDto;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.language.repo.LanguageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepo languageRepo;

    public List<LanguageDto>  findAll() {
        List<LanguageEntity> result = languageRepo.findAll();
        return result.stream()
                .map(lang -> new LanguageDto(
                        lang.getId(),
                        lang.getCode(),
                        lang.getLanguage()
                )).toList();
    }
}
