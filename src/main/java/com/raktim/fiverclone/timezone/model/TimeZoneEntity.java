package com.raktim.fiverclone.timezone.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@Table(name = "timezone")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TimeZoneEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;
}
