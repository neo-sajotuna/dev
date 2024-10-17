package com.sparta.newneoboardbuddy.domain.workspace.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.member.rpository.MemberRepository;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.InviteMemberRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.GetWorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.UpdateWorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import com.sparta.newneoboardbuddy.domain.workspace.exception.EmailNotFoundException;
import com.sparta.newneoboardbuddy.domain.workspace.exception.UnauthorizedActionException;
import com.sparta.newneoboardbuddy.domain.workspace.exception.UserAlreadyMemberException;
import com.sparta.newneoboardbuddy.domain.workspace.exception.WorkspaceNotFoundException;
import com.sparta.newneoboardbuddy.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    /**
     * 워크 스페이스를 생성하는 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param workspaceRequest 워크 스페이스 생성에 필요한 workspace이름, 설명이 담긴 Request
     * @return 생성된 Workspace 정보가 담긴 Dto 객체
     */
    @Transactional
    public WorkspaceResponse createWorkspace(AuthUser authUser, WorkspaceRequest workspaceRequest) {
        User user = User.fromAuthUser(authUser);

        //    * 워크스페이스는 ADMIN 권한을 가진 유저만 생성할 수 있습니다.
        if (user.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedActionException("워크스페이스를 생성할 권한이 없습니다.");
        }

        Workspace workspace = new Workspace(workspaceRequest);

        // 워크스페이스 만든 유저를 멤버로 등록
        Member member = new Member(user, workspace, MemberRole.WORKSPACE_MEMBER);
        memberRepository.save(member);

        workspaceRepository.save(workspace);
        return new WorkspaceResponse(workspace);

    }

    /**
     * 워크 스페이스에 유저를 초대하는 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 해당 유저를 추가할 Workspace Id
     * @param inviteMemberRequest 유저 추가에 필요한 Email과 권한 내용이 담긴 Request
     */
    @Transactional
    public void inviteMember(AuthUser authUser, Long spaceId, InviteMemberRequest inviteMemberRequest) {
        User user = User.fromAuthUser(authUser);
        Workspace workspace = workspaceRepository.findById(spaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("해당 워크스페이스가 없습니다"));

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 워크스페이스에 대한 권한이 없습니다."));


        // 멤버 초대는 워크스페이스 역할을 가진 멤버만 가능
        if (member.getMemberRole() != MemberRole.WORKSPACE_MEMBER) {
            throw new UnauthorizedActionException("워크스페이스에 멤버 초대할 권한이 없습니다.");
        }

        // 존재하지 않는 이메일로 초대하는 경우
        User invitedUser = userRepository.findByEmail(inviteMemberRequest.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("해당 이메일의 유저가 존재하지 않습니다."));

        // 중복 초대 방지 (이미 해당 워크스페이스에 속한 멤버인지 확인)
        if(memberRepository.existsByUserAndWorkspace(invitedUser, member.getWorkspace())) {
            throw new UserAlreadyMemberException("해당 유저는 이미 이 워크스페이스의 멤버입니다.");
        }

        // 새로운 멤버 생성 및 저장
        Member newMember = new Member(invitedUser, member.getWorkspace(), inviteMemberRequest.getMemberRole());
        memberRepository.save(newMember);

    }

    /**
     * 해당 유저가 가입되어 있는 워크 스페이스 목록을 페이징하여 조회하는 메서드
     * @param page 조회할 페이지
     * @param size 페이지당 크기
     * @param authUser Filter에서 인증 완료된 유저
     * @return 페이징된 해당 유저가 가입되어 있는 워크 스페이스 목록
     */
    @Transactional
    public Page<GetWorkspaceResponse> getWorkspace(int page, int size, AuthUser authUser) {
        Pageable pageable = PageRequest.of(page-1, size);
        User user = User.fromAuthUser(authUser);

        // 유저가 멤버로 가입된 워크스페이스 목록 조회
        Page<Member> memberPage = memberRepository.findAllByUser(user, pageable);

        // Page<Member>를 Page<GetWorkspaceResponse>로 변환
        return memberPage.map(member -> new GetWorkspaceResponse(member.getWorkspace()));

    }

    /**
     * space Id에 해당하는 workspace를 갱신 / 수정하는 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 수정할 대상의 space Id
     * @param workspaceRequest workspace 수정에 필요한 스페이스 제목, 설명이 담긴 Request
     * @return 갱신된 Workspace 정보가 담긴 Dto 객체
     */
    @Transactional
    public UpdateWorkspaceResponse updateWorkspace(AuthUser authUser, Long spaceId, WorkspaceRequest workspaceRequest) {
        User user = User.fromUser(authUser);

        Workspace workspace = workspaceRepository.findById(spaceId)
                .orElseThrow(() -> new  WorkspaceNotFoundException("해당 ID의 워크스페이스를 찾을 수 없습니다."));

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 워크스페이스에 대한 권한이 없습니다."));

        // 유저가 워크스페이스를 수정할 권한이 없는 경우 (예: WORKSPACE_MEMBER 아닌 경우)
        if (member.getMemberRole() != MemberRole.WORKSPACE_MEMBER) {
            throw new UnauthorizedActionException("워크스페이스를 수정할 권한이 없습니다.");
        }

        workspace.updateWorkspace(workspaceRequest.getSpaceName(), workspaceRequest.getContent());
        return new UpdateWorkspaceResponse(workspace.getSpaceId(), workspace.getSpaceName(), workspace.getContent());

    }

    /**
     * 해당 인증된 유저가 속해있는 workspace를 삭제하는 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 삭제할 대상의 space Id
     */
    @Transactional
    public void deleteWorkspace(AuthUser authUser, Long spaceId) {
        User user = User.fromUser(authUser);

        Workspace workspace = workspaceRepository.findById(spaceId)
                .orElseThrow(() -> new  WorkspaceNotFoundException("해당 ID의 워크스페이스를 찾을 수 없습니다."));

        // 워크스페이스와 유저가 연결된 멤버 정보를 조회
        Member member = memberRepository.findByUserAndWorkspace(user, workspace)
                .orElseThrow(() -> new UnauthorizedActionException("해당 워크스페이스에 대한 권한이 없습니다."));

        // 유저가 워크스페이스를 수정할 권한이 없는 경우 (예: WORKSPACE_MEMBER 아닌 경우)
        if (member.getMemberRole() != MemberRole.WORKSPACE_MEMBER) {
            throw new UnauthorizedActionException("워크스페이스를 삭제 권한이 없습니다.");
        }

        workspaceRepository.delete(workspace);
    }
}

