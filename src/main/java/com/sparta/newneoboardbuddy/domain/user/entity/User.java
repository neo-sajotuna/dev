package com.sparta.newneoboardbuddy.domain.user.entity;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isActive = true;

    public User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public User(Long id) {
        this.id = id;
    }


    public static User fromAuthUser(AuthUser authUser) {
        // SimpleGrantedAuthority에서 권한 문자열을 가져와서 UserRole로 변환
        String role = ((SimpleGrantedAuthority) authUser.getAuthorities().toArray()[0]).getAuthority();
        UserRole userRole = UserRole.valueOf(role); // UserRole Enum과 매칭

        return new User(authUser.getId(), authUser.getEmail(), userRole);
    }

    public static User fromUser(AuthUser authUser) {
        return new User(authUser.getId());
    }
    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void withdraw() { this.isActive = false; }
}
