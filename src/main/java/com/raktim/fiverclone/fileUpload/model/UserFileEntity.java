package com.raktim.fiverclone.fileUpload.model;

import com.raktim.fiverclone.common.entities.BaseEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileType;
import com.raktim.fiverclone.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_files")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserFileEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String s3Key;
    private String originalFileName;
    private String contentType;

    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(30) check (type in ('PROFILE_PICTURE','GIG_IMAGE'))"
    )
    private FileType type;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "varchar(20) check (status in ('UPLOADING', 'UPLOADED', 'DELETED'))"
    )
    private FileStatus status = FileStatus.UPLOADING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
