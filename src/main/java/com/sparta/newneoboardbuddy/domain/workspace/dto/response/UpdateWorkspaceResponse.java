package com.sparta.newneoboardbuddy.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class UpdateWorkspaceResponse {
    private Long spaceId;
    private String spaceName;
    private String content;

    public UpdateWorkspaceResponse(Long spaceId, String content, String spaceName) {
        this.spaceId = spaceId;
        this.content = content;
        this.spaceName = spaceName;
    }
}
