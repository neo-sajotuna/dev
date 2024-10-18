package com.sparta.newneoboardbuddy.domain.auth.controller;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.dto.response.CommonResponseDto;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.SigninRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.SignupRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.WithdrawRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SigninResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SignupResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.WithdrawResponse;
import com.sparta.newneoboardbuddy.domain.auth.service.AuthService;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입을 진행할 Controller 메서드
     * @param signupRequest 회원가입에 필요한 정보가 담긴 Request
     * @return 회원가입 성공 시 : 200 OK + 회원 정보가 담긴 Dto / 그 외 : ErrorCode + Description
     */
    @PostMapping("/auth/signup")
    public CommonResponseDto<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return CommonResponseDto.success(authService.signup(signupRequest));
    }

    /**
     * 로그인을 진행할 Controller 메서드
     * @param signinRequest 로그인에 필요한 정보가 담긴 Request
     * @return 로그인 성공 시 : 200 OK + 로그인된 회원 정보가 담긴 Dto / 그 외 : ErrorCode + Description
     */
    @PostMapping("/auth/signin")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }

    /**
     * 회원 탈퇴를 진행할 Controller 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param withdrawRequest 회원 탈퇴에 필요한 정보가 담긴 Request
     * @return 회원 탈퇴 성공 시 : 200 OK + 탈퇴된 회원 Id / 그 외 : ErrorCode + Description
     */
    @DeleteMapping("/auth/withdraw")
    public WithdrawResponse withdraw(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody WithdrawRequest withdrawRequest) {
        return authService.withdraw(authUser, withdrawRequest);
    }

    @GetMapping("/myinfo")
    public SignupResponse myinfo(@AuthenticationPrincipal AuthUser authUser) {
        return new SignupResponse(User.fromAuthUser(authUser));
    }
}
