package com.sparta.newneoboardbuddy.domain.comment.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentUpdateRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String comment;

}
