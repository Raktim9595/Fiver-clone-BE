package com.raktim.fiverclone.common;

import com.raktim.fiverclone.common.utils.EntityReferenceResolver;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntityReferenceResolverTest {
    @Mock
    private EntityManager entityManager;

    private EntityReferenceResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new EntityReferenceResolver(entityManager);
    }

    @Test
    @DisplayName("""
            Given method getRequired when called,
            And entity exists in the db,
            Then it should return the found entity
            """)
    public void shouldReturnEntity() {
        UUID id = UUID.randomUUID();

        SellerApplicationEntity application =
                new SellerApplicationEntity();

        when(
                entityManager.find(
                        SellerApplicationEntity.class,
                        id
                )
        ).thenReturn(application);

        SellerApplicationEntity result =
                resolver.getRequired(
                        SellerApplicationEntity.class,
                        id
                );

        assertSame(application, result);
    }

    @Test
    @DisplayName("""
            Given method getRequired when called,
            And entity does not exists in the db,
            Then it should return ENTITY_NOT_FOUND exception
            """)
    public void shouldThrowBusinessException() {
        UUID id = UUID.randomUUID();

        when(
                entityManager.find(
                        SellerApplicationEntity.class,
                        id
                )
        ).thenReturn(null);

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.NOT_FOUND,
                "ENTITY_NOT_FOUND",
                "%s not found with id %s".formatted(SellerApplicationEntity.class.getSimpleName(), id),
                () -> resolver.getRequired(SellerApplicationEntity.class, id)
        );
    }
}
