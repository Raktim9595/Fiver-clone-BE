package com.raktim.fiverclone.country.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "country")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CountryEntity {
    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column
    private String name;

    @Column(name = "phone_code")
    private String phoneCode;


}
