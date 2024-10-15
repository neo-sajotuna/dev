package com.sparta.newneoboardbuddy.domain.list.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListUpdateRequest {
    Long index;
    Long workspaceId;
    String title;
}
