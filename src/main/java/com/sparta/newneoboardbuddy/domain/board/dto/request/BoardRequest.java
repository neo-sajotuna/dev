package com.sparta.newneoboardbuddy.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString   //asfasfdfasdf
public class BoardRequest {
    private Long spaceId;
    private String boardTitle;
    private String background;

}
