package com.sparta.newneoboardbuddy.domain.file.dto.request;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileUploadDto {

    private final Long targetId;

    private final String targetTable;

    private final MultipartFile targetFile;

}

