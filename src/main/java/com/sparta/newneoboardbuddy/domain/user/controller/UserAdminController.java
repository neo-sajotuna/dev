package com.sparta.newneoboardbuddy.domain.user.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.enums.MemberRole;
import com.sparta.newneoboardbuddy.domain.user.dto.AdminUserResponse;
import com.sparta.newneoboardbuddy.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
