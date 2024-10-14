package com.sparta.newneoboardbuddy.domain.card.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class CardCreateResponse {
    private final Long cardId;
    private final String cardTitle;
    private final String cardContent;
    private final LocalTime finishedAt;
}
