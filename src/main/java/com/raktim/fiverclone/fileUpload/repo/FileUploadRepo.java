package com.raktim.fiverclone.fileUpload.repo;

import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileUploadRepo extends JpaRepository<UserFileEntity, UUID> {
    Optional<UserFileEntity> findByIdAndUserId(UUID id, UUID userId);
    List<UserFileEntity> findAllByUser_IdAndStatusAndType(
            UUID userId,
            FileStatus status,
            FileType type
    );
}
