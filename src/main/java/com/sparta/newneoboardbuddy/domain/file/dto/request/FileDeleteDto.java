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

    private final Long targetId;

    private final String targetTable;

}

