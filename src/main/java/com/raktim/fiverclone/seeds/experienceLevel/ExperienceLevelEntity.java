package com.raktim.fiverclone.seeds.experienceLevel;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "experience_level")
@Data
public class ExperienceLevelEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(20) check (experience_level in ('BEGINNER','INTERMEDIATE', 'PRO'))"
    )
    private ExperienceLevel experienceLevel;
}
