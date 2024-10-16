package com.sparta.newneoboardbuddy.domain.board.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GetBoardListResponse {
    private Long listId;
    private String title;
    private List<GetCardResponse> cards;

    public GetBoardListResponse(Long listId, String title, List<GetCardResponse> cardResponses) {
        this.listId = listId;
        this.title = title;
        this.cards= cardResponses;
    }
}
