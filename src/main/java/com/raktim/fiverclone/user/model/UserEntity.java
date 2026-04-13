package com.raktim.fiverclone.user.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

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

    @Min(0)
    private int age;

    private String address;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(20) check (role in ('BUYER','ADMIN', 'SELLER'))"
    )
    private UserRole role = UserRole.BUYER;
}
