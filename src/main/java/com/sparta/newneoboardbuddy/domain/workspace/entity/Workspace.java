package com.sparta.newneoboardbuddy.domain.workspace.entity;

import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "workspace")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spaceId;

    private String spaceName;

    private String content;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "workspace",cascade = CascadeType.ALL)
    private List<Member> members = new ArrayList<>();


    public Workspace(WorkspaceRequest workspaceRequest) {
        this.spaceName = workspaceRequest.getSpaceName();
        this.content = workspaceRequest.getContent();
    }

    public void updateWorkspace(String spaceName, String content) {
        this.spaceName = spaceName;
        this.content = content;
    }
}
