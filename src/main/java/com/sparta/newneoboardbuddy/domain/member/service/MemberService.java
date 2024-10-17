package com.sparta.newneoboardbuddy.domain.member.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * 다음 조건을 만족하는 로그인된 유저와 관련되어 있는 Member 객체를 반환하는 메서드
     *  1. Workspace Id를 갖고 있는 Workspace에 속해 있어야 한다.
     *  2. 해당 멤버는 읽기 전용이 아닌 다른 권한을 갖고 있어야 한다.
     *
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @param workspaceId 해당 유저가 속해 있는지 확인할 workSpace Id
     * @return 조건을 만족하는 유저와 관련된 Member 객체
     */
    public Member verifyMember(AuthUser authUser, Long workspaceId) {
        User user = User.fromUser(authUser);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new NoSuchElementException("워크스페이스 없다"));

        Member member = memberRepository.findByUserAndWorkspace(user,workspace)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        // 읽기 전용 유저 생성 못하게 예외처리 해야함
        if (member.getMemberRole() == MemberRole.READ_ONLY_MEMBER){
            throw new InvalidRequestException("읽기 전용 멤버는 읽기를 제외한 작업을 진행할 수 없습니다.");
        }

        return member;
    }

    /**
     * 해당 workspace에 속한 user와 관련된 Member 객체를 workspace까지 fetch Join 한 상태로 반환하는 메서드
     * @param authUser Filter에서 인증이 완료된 유저 정보
     * @param workspaceId 해당 유저가 속해 있는지 확인할 workSpace Id
     * @return Workspace까지 fetch Join 한 상태의 Member 객체
     */
    public Member memberInWorkspaceFetchWorkspace(AuthUser authUser, Long workspaceId) {
        User user = User.fromUser(authUser);

        return memberRepository.findByUserIdWithJoinFetchWorkspace(user.getId(), workspaceId)
                .orElseThrow(()->new NoSuchElementException("워크 스페이스에 가입된 정보가 없습니다."));
    }
}
