package com.sparta.newneoboardbuddy.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentSaveRequestDto {

    @NotNull(message = "카드 id 값은 필수 파라미터 입니다.")
    private Long cardId;

    @NotBlank(message = "comment 값은 필수 파라미터 입니다.")
    private String comment;

}
