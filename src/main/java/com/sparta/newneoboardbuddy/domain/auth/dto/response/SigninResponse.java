package com.sparta.newneoboardbuddy.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponse {
    String bearerToken;

    public SigninResponse(String token) {
        this.bearerToken = token;
    }
}
