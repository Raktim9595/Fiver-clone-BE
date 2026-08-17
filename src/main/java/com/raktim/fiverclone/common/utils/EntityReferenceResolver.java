package com.raktim.fiverclone.common.utils;

import com.raktim.fiverclone.common.exceptions.BusinessException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityReferenceResolver {

    private final EntityManager entityManager;

    public <T> T getReference(Class<T> entityClass, UUID id) {
        return entityManager.getReference(entityClass, id);
    }
    public <T> T getRequired(Class<T> entityClass, UUID id) {
        T entity = entityManager.find(entityClass, id);

        if (entity == null) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "ENTITY_NOT_FOUND",
                    "%s not found with id %s"
                            .formatted(entityClass.getSimpleName(), id)
            );
        }

        return entity;
    }
}