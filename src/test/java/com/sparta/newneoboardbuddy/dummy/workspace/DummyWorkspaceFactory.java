package com.sparta.newneoboardbuddy.dummy.workspace;

import com.github.javafaker.Faker;
import com.sparta.newneoboardbuddy.domain.board.dto.request.BoardRequest;
import com.sparta.newneoboardbuddy.domain.board.entity.Board;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyWorkspaceFactory {
    /**
     * Random한 Workspace 데이터를 만들어주는 메서드
     * @param size DummyData 개수
     * @param users 해당 workspace 생성자로 등록할 user
     * @return 완성된 workspace dummyData
     */
    public List<Workspace> createDummyWorkspace(Faker faker, int size, List<User> users, List<Member> outputMembers) {
        List<Workspace> workspaces = new ArrayList<Workspace>();

        for (int i = 0; i < size; i++) {
            WorkspaceRequest workspaceRequest = new WorkspaceRequest(faker.team().name(), faker.programmingLanguage().name());
            Workspace workspace = new Workspace(workspaceRequest);

            int next = faker.random().nextInt(users.size());

            outputMembers.add(new Member(users.get(next), workspace, MemberRole.WORKSPACE_MEMBER));
            workspaces.add(workspace);
        }

        return workspaces;
    }
}
