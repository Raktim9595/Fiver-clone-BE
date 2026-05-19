package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.fileUpload.dto.FileUploadDto;
import com.raktim.fiverclone.fileUpload.model.UserFileEntity;
import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import com.raktim.fiverclone.fileUpload.utils.FileType;

public class FileUploadTestData {
    public static FileUploadDto.FileUploadDtoBuilder validFileUploadDto() {
        return FileUploadDto.builder()
                .fileSize((long) 4400)
                .fileName("File one")
                .type(FileType.PROFILE_PICTURE)
                .contentType("img");
    }

    public static UserFileEntity.UserFileEntityBuilder validUserFileEntity() {
        return UserFileEntity.builder()
                .originalFileName("File one")
                .s3Key("s3key")
                .status(FileStatus.UPLOADING)
                .fileSize((long) 4400)
                .type(FileType.PROFILE_PICTURE)
                .contentType("img");
    }

}
