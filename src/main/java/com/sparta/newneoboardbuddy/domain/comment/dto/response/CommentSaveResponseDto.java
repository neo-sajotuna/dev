package com.sparta.newneoboardbuddy.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentSaveResponseDto {

    private final Long commentId;

    private final LocalDateTime createdAt;

}
