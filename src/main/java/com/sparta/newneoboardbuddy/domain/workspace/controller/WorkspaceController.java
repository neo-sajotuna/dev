package com.sparta.newneoboardbuddy.domain.workspace.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.newneoboardbuddy.domain.workspace.dto.response.*;
import com.sparta.newneoboardbuddy.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // 워크스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace (@AuthenticationPrincipal AuthUser authUser,
                                                              @RequestBody WorkspaceRequest workspaceRequest) {
        return ResponseEntity.ok(workspaceService.createWorkspace(authUser, workspaceRequest));
    }

    // 워크스페이스 멤버 초대
    @PostMapping("/{spaceId}/member")
    public ResponseEntity<String> inviteMember (@AuthenticationPrincipal AuthUser authUser,
                                                @PathVariable Long spaceId,
                                                @RequestParam String email,
                                                @RequestParam MemberRole memberRole ) {
        workspaceService.inviteMember(authUser, spaceId, email, memberRole);
        return ResponseEntity.ok().body("워크스페이스에 해당 멤버를 초대하였습니다.");
    }

    // 워크스페이스 조회
    // 유저가 멤버로 가입된 워크스페이스 목록을 볼 수 있습니다.
    @GetMapping
    public ResponseEntity<Page<GetWorkspaceResponse>> getWorkspace ( @RequestParam(defaultValue = "1", required = false) int page,
                                                                     @RequestParam(defaultValue = "10", required = false) int size,
                                                                     @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(workspaceService.getWorkspace(page, size, authUser));
    }

    //    * 워크스페이스의 수정 및 삭제는 워크스페이스 역할을 가진 멤버만 할 수 있습니다.
    //    * 삭제 시 워크스페이스 내의 모든 보드와 데이터도 삭제됩니다.
    // 워크스페이스 수정
    @PutMapping("/{spaceId}")
    public ResponseEntity<UpdateWorkspaceResponse> updateWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                   @PathVariable Long spaceId,
                                                                   @RequestBody WorkspaceRequest workspaceRequest) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(authUser, spaceId, workspaceRequest));
    }


    //    * 워크스페이스의 수정 및 삭제는 워크스페이스 역할을 가진 멤버만 할 수 있습니다.
    //    * 삭제 시 워크스페이스 내의 모든 보드와 데이터도 삭제됩니다.
    // 워크스페이스 삭제
    @DeleteMapping("/{spaceId}")
    public ResponseEntity<String> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser,
                                                                   @PathVariable Long spaceId) {
        workspaceService.deleteWorkspace(authUser, spaceId);
        return ResponseEntity.ok().body("해당 워크스페이스가 삭제되었습니다.");
    }

}
