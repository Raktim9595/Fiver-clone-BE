package com.raktim.fiverclone.seeds.skills;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "skills")
public class SkillEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Skill skill;
}
