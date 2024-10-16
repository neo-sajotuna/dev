package com.sparta.newneoboardbuddy.domain.board.dto.request;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponse {
    private Long spaceId;
    private Long boardId;
    private String boardTitle;
    private String background;


    public BoardResponse(Board board) {
        this.spaceId = board.getWorkspace().getSpaceId();
        this.boardId = board.getBoardId();
        this.boardTitle = board.getBoardTitle();
        this.background = board.getBackground();
    }
}
