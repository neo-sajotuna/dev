package com.sparta.newneoboardbuddy.domain.user.controller;

import com.sparta.newneoboardbuddy.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;

//    @PostMapping("/admin/users/{userId}")
//    public ResponseEntity<AdminUserResponse> giveMemberType(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long userId, @RequestBody MemberRole role){
//        userAdminService.giveMemberType(authUser, userId, role);
//    }
}
