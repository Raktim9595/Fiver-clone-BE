package com.raktim.fiverclone.sellerApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "occupations")
@Builder(toBuilder = true)
public class OccupationEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;
}
