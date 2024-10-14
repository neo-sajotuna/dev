package com.sparta.newneoboardbuddy.common.dto;

import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String email, UserRole role) {
        this.id = userId;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}