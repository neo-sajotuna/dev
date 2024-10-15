package com.sparta.newneoboardbuddy.domain.list.dto.response;

import com.sparta.newneoboardbuddy.domain.list.entity.BoardList;
import lombok.Getter;

@Getter
public class BoardListResponse {
    Long id;
    String title;
    Long index;

    public BoardListResponse(BoardList boardList) {
        this.id = boardList.getListId();
        this.title = boardList.getTitle();
        this.index = boardList.getListIndex();
    }
}
