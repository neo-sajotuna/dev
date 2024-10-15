package com.sparta.newneoboardbuddy.domain.list.dto.request;

import lombok.Getter;

@Getter
public class BoardListRequest {
    Long workspaceId;
    Long boardId;
    String title;
}
