package com.sparta.newneoboardbuddy.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
    private Long spaceId;
    private String boardTitle;
    private String background;

}
