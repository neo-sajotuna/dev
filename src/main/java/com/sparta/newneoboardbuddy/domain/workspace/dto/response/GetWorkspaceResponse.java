package com.sparta.newneoboardbuddy.domain.workspace.dto.response;

import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import lombok.Getter;

@Getter
public class GetWorkspaceResponse {

    private Long spaceId;
    private String spaceName;
    private String content;

    public GetWorkspaceResponse(Workspace workspace) {
        this.spaceId = workspace.getSpaceId();
        this.spaceName = workspace.getSpaceName();
        this.content = workspace.getContent();
    }
}
