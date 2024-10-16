package com.sparta.newneoboardbuddy.domain.board.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GetBoardResponse {
    private Long boardId;
    private String boardTitle;
    private String background;
    private List<GetBoardListResponse> boardLists;

    public GetBoardResponse(Long boardId, String boardTitle, String background, List<GetBoardListResponse> boardListResponses) {
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.background = background;
        this.boardLists = boardListResponses;
    }
}
