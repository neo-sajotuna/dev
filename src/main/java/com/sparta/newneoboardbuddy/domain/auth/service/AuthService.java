package com.sparta.newneoboardbuddy.domain.auth.service;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.common.exception.InvalidRequestException;
import com.sparta.newneoboardbuddy.config.JwtUtil;
import com.sparta.newneoboardbuddy.config.SlackNotificationUtil;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.SigninRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.SignupRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.request.WithdrawRequest;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SigninResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.SignupResponse;
import com.sparta.newneoboardbuddy.domain.auth.dto.response.WithdrawResponse;
import com.sparta.newneoboardbuddy.domain.auth.exception.AuthException;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import com.sparta.newneoboardbuddy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SlackNotificationUtil slackNotificationUtil;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 처리 할 메서드
     * @param signupRequest 회원가입에 필요한 정보가 담긴 Request
     * @return 회원가입된 유저 정보가 담긴 Dto
     */
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = new User(
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );

        User savedUser = userRepository.save(newUser);

        slackNotificationUtil.sendNewUser(savedUser);

        return new SignupResponse(savedUser);
    }

    /**
     * 로그인 처리하는 메서드
     * @param signinRequest 로그인에 필요한 유저 정보가 담긴 Request
     * @return 로그인된 유저 정보가 담긴 Dto객체
     */
    @Transactional
    public SigninResponse signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new SigninResponse(bearerToken);
    }

    /**
     * 회원 탈퇴 처리하는 메서드
     * @param authUser Filter에서 인증된 유저 정보
     * @param withdrawRequest 회원 탈퇴에 필요한 비밀번호가 담긴 Request
     * @return 삭제된 User Id가 담긴 Dto객체
     */
    @Transactional
    public WithdrawResponse withdraw(AuthUser authUser, WithdrawRequest withdrawRequest) {
        User user = userRepository.findByEmail(authUser.getEmail()).orElseThrow(
                () -> new InvalidRequestException("가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(withdrawRequest.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }

        user.withdraw();

        return new WithdrawResponse(user.getId());
    }
}
