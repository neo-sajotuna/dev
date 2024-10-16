package com.sparta.newneoboardbuddy.domain.board.dto.response;

import lombok.Getter;

@Getter
public class GetCardResponse {
    private Long cardId;
    private String cardTitle;
    private String cardContent;

    public GetCardResponse(Long cardId, String cardTitle, String cardContent) {
        this.cardId = cardId;
        this.cardTitle = cardTitle;
        this.cardContent = cardContent;
    }
}
