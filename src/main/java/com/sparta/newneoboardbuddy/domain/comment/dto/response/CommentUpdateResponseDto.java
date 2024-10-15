package com.sparta.newneoboardbuddy.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentUpdateResponseDto {

    private final Long commentId;

    private final String comment;

    private final LocalDateTime updatedAt;

}
