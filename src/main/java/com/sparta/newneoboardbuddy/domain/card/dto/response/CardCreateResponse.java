package com.sparta.newneoboardbuddy.domain.card.dto.response;

import com.sparta.newneoboardbuddy.domain.card.entity.Card;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateResponse {
    private Long cardId;
    private String cardTitle;
    private String cardContent;
    private LocalTime startedAt;
    private LocalTime finishedAt;
    private Long memberId;

    public CardCreateResponse(Card card){
        this.cardId= card.getCardId();
        this.cardTitle= card.getCardTitle();
        this.cardContent= card.getCardContent();
        this.startedAt= card.getStartedAt();
        this.finishedAt= card.getFinishedAt();
        this.memberId = card.getMember().getMemberId();
    }
}
