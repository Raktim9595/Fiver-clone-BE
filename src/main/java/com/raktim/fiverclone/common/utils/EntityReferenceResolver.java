package com.raktim.fiverclone.common.utils;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityReferenceResolver {

    private final EntityManager entityManager;

    public <T> T getReference(Class<T> entityClass, UUID id) {
        return entityManager.getReference(entityClass, id);
    }
}