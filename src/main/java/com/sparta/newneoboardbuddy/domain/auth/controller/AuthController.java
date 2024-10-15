package com.sparta.newneoboardbuddy.domain.auth.controller;

import com.sparta.newneoboardbuddy.domain.auth.dto.request.SigninRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.SignupRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.WithdrawRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SigninResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SignupResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.WithdrawResponse;
import com.sparta.newneoboardbuddy.domain.auth.service.AuthService;
import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auth/signin")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }

    @DeleteMapping("/auth/withdraw")
    public WithdrawResponse withdraw(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody WithdrawRequest withdrawRequest) {
        return authService.withdraw(authUser, withdrawRequest);
    }
}
