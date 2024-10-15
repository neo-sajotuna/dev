package com.sparta.newneoboardbuddy.domain.card.dto.response;

import com.sparta.newneoboardbuddy.domain.cardActivityLog.entity.CardActivityLog;
import com.sparta.newneoboardbuddy.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<CardActivityLog> activityLogs;
    private List<Comment> comments;

}
