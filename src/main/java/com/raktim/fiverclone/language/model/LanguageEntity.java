package com.raktim.fiverclone.language.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "language")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LanguageEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "varchar(3)"
    )
    private String code;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "varchar(20)"
    )
    private String language;
}
