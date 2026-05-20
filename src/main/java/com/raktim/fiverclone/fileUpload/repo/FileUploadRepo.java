package com.raktim.fiverclone.fileUpload.repo;

import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileUploadRepo extends JpaRepository<UserFileEntity, UUID> {
}
