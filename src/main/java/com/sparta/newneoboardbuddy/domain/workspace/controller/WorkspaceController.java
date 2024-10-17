package com.sparta.newneoboardbuddy.domain.workspace.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.InviteMemberRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.GetWorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.UpdateWorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.newneoboardbuddy.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * 워크 스페이스 생성하는 Controller 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param workspaceRequest 워크 스페이스 생성에 필요한 workspace이름, 설명이 담긴 Request
     * @return 성공시 200 OK + 워크스페이스 정보 / 그 외 ErrorCode + Description
     */
    @PostMapping
    public CommonResponseDto<WorkspaceResponse> createWorkspace (@AuthenticationPrincipal AuthUser authUser,
                                                                 @RequestBody WorkspaceRequest workspaceRequest) {
        return CommonResponseDto.success(workspaceService.createWorkspace(authUser, workspaceRequest));
    }

    /**
     * 워크 스페이스에 유저를 초대하는 Controller 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 해당 유저를 초대할 Workspace Id
     * @param inviteMemberRequest 유저 초대에 필요한 Email과 권한 내용이 담긴 Request
     * @return 성공시 200 OK + 초대 성공 메시지 / 그 외 ErrorCode + Description
     */
    @PostMapping("/{spaceId}/member")
    public CommonResponseDto<String> inviteMember (@AuthenticationPrincipal AuthUser authUser,
                                                   @PathVariable Long spaceId,
                                                   @RequestBody InviteMemberRequest inviteMemberRequest) {
        workspaceService.inviteMember(authUser, spaceId, inviteMemberRequest);
        return CommonResponseDto.success("워크스페이스에 해당 멤버를 초대하였습니다.");
    }

    /**
     * 해당 유저가 가입되어 있는 워크 스페이스 목록을 페이징하여 조회하는 Controller 메서드
     * @param page 조회할 페이지
     * @param size 페이지당 크기
     * @param authUser Filter에서 인증 완료된 유저
     * @return 성공시 200 OK + 페이징된 Workspace 정보들 / 그 외 ErrorCode + Description
     */
    @GetMapping
    public CommonResponseDto<Page<GetWorkspaceResponse>> getWorkspace ( @RequestParam(defaultValue = "1", required = false) int page,
                                                                        @RequestParam(defaultValue = "10", required = false) int size,
                                                                        @AuthenticationPrincipal AuthUser authUser) {
        return CommonResponseDto.success(workspaceService.getWorkspace(page, size, authUser));
    }

    /**
     * space Id에 해당하는 workspace를 갱신 / 수정하는 Controller 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 수정할 대상의 space Id
     * @param workspaceRequest workspace 수정에 필요한 스페이스 제목, 설명이 담긴 Request
     * @return 성공시 200 OK + 수정된 workspace 정보 / 실패시 ErrorCode + Description
     */
    @PutMapping("/{spaceId}")
    public CommonResponseDto<UpdateWorkspaceResponse> updateWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                      @PathVariable Long spaceId,
                                                                      @RequestBody WorkspaceRequest workspaceRequest) {
        return CommonResponseDto.success(workspaceService.updateWorkspace(authUser, spaceId, workspaceRequest));
    }

    /**
     * 해당 인증된 유저가 속해있는 workspace를 삭제하는 Controller 메서드
     * @param authUser Filter에서 인증 완료된 유저
     * @param spaceId 수정할 대상의 space Id
     * @return 성공시 200 OK + 메시지 / 실패시 ErrorCode + Description
     */
    @DeleteMapping("/{spaceId}")
    public CommonResponseDto<String> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                     @PathVariable Long spaceId) {
        workspaceService.deleteWorkspace(authUser, spaceId);
        return CommonResponseDto.success("해당 워크스페이스가 삭제되었습니다.");
    }
}

