package com.raktim.fiverclone.user.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String address;
    private String phoneNumber;
    private String bio;
    private String timeZone;
    private String country;
    private String language;
    private LocalDate dateOfBirth;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(20) check (status in ('ACTIVE', 'DISABLED', 'DELETED'))"
    )
    private UserStatus status =  UserStatus.ACTIVE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(20) check (role in ('BUYER','ADMIN', 'SELLER'))"
    )
    private UserRole role = UserRole.BUYER;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserFileEntity> files = new ArrayList<>();
}
