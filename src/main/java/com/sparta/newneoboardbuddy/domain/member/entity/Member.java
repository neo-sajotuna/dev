package com.sparta.newneoboardbuddy.domain.member.entity;

import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Member")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole; // WORKSPACE_MEMBER, BOARD_MEMBER, READ_ONLY_MEMBER

    // User, Workspace, MemberRole 을 받아서 Member 생성
    public Member(User user, Workspace workspace, MemberRole memberRole) {
        this.user = user;
        this.workspace = workspace;
        this.memberRole = memberRole;
    }
}
