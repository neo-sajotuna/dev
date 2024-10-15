package com.sparta.newneoboardbuddy.domain.card.dto.response;

import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CardUpdateResponse {
    private final Long cardId;
    private final String cardTitle;
    private final String cardContent;
    private final Long memberId;
    private final LocalDateTime activeTime;

}
