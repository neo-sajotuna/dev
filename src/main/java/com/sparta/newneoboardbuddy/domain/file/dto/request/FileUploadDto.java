package com.sparta.newneoboardbuddy.domain.file.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

