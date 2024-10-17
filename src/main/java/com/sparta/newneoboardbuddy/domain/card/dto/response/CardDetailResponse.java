package com.sparta.newneoboardbuddy.domain.card.dto.response;

import com.sparta.newneoboardbuddy.domain.cardActivityLog.logResponse.LogResponseDto;
import com.sparta.newneoboardbuddy.domain.comment.dto.response.CommentSaveResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailResponse {
    private Long cardId;
    private String cardTitle;
    private String cardContent;
    private LocalTime startedAt;
    private LocalTime finishedAt;
    private LogResponseDto activityLogDto;
    private List<CommentSaveResponseDto> comments;

}
