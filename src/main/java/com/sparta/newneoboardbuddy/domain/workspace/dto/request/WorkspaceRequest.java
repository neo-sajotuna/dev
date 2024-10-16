package com.sparta.newneoboardbuddy.domain.workspace.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceRequest {
    private String spaceName;
    private String content;

}
