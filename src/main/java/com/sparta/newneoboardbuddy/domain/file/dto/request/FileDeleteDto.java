package com.sparta.newneoboardbuddy.domain.file.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileDeleteDto {

    private Long workspaceId;

    private Long targetId;

    private String targetTable;

}

