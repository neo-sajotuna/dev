package com.sparta.newneoboardbuddy.domain.workspace.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.InviteMemberRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.*;
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

    // 워크스페이스 생성
    @PostMapping
    public CommonResponseDto<WorkspaceResponse> createWorkspace (@AuthenticationPrincipal AuthUser authUser,
                                                                 @RequestBody WorkspaceRequest workspaceRequest) {
        return CommonResponseDto.success(workspaceService.createWorkspace(authUser, workspaceRequest));
    }

    // 워크스페이스 멤버 초대
    @PostMapping("/{spaceId}/member")
    public CommonResponseDto<String> inviteMember (@AuthenticationPrincipal AuthUser authUser,
                                                @PathVariable Long spaceId,
                                                @RequestBody InviteMemberRequest inviteMemberRequest) {
        workspaceService.inviteMember(authUser, spaceId, inviteMemberRequest);
        return CommonResponseDto.success("워크스페이스에 해당 멤버를 초대하였습니다.");
    }

    // 워크스페이스 조회
    // 유저가 멤버로 가입된 워크스페이스 목록을 볼 수 있습니다.
    @GetMapping
    public CommonResponseDto<Page<GetWorkspaceResponse>> getWorkspace ( @RequestParam(defaultValue = "1", required = false) int page,
                                                                     @RequestParam(defaultValue = "10", required = false) int size,
                                                                     @AuthenticationPrincipal AuthUser authUser) {
        return CommonResponseDto.success(workspaceService.getWorkspace(page, size, authUser));
    }

    //    * 워크스페이스의 수정 및 삭제는 워크스페이스 역할을 가진 멤버만 할 수 있습니다.
    //    * 삭제 시 워크스페이스 내의 모든 보드와 데이터도 삭제됩니다.
    // 워크스페이스 수정
    @PutMapping("/{spaceId}")
    public CommonResponseDto<UpdateWorkspaceResponse> updateWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                   @PathVariable Long spaceId,
                                                                   @RequestBody WorkspaceRequest workspaceRequest) {
        return CommonResponseDto.success(workspaceService.updateWorkspace(authUser, spaceId, workspaceRequest));
    }


    //    * 워크스페이스의 수정 및 삭제는 워크스페이스 역할을 가진 멤버만 할 수 있습니다.
    //    * 삭제 시 워크스페이스 내의 모든 보드와 데이터도 삭제됩니다.
    // 워크스페이스 삭제
    @DeleteMapping("/{spaceId}")
    public CommonResponseDto<String> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                   @PathVariable Long spaceId) {
        workspaceService.deleteWorkspace(authUser, spaceId);
        return CommonResponseDto.success("해당 워크스페이스가 삭제되었습니다.");
    }

}
