package com.sparta.newneoboardbuddy.domain.workspace.dto.response;

import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WorkspaceResponse {
    private Long spaceId;
    private String spaceName;
    private String content;


    public WorkspaceResponse(Workspace workspace) {
        this.spaceId = workspace.getSpaceId();
        this.spaceName = workspace.getSpaceName();
        this.content = workspace.getContent();
    }
}
