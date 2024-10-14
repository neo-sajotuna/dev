package com.sparta.newneoboardbuddy.auth.dto.response;

import com.sparta.newneoboardbuddy.domain.user.entity.User;

public class SignupResponse {
    Long userId;
    String email;
    String userRole;

    public SignupResponse(User user) {
        userId = user.getId();
        email = user.getEmail();
        userRole = user.getUserRole().name();
    }
}
