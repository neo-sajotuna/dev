package com.sparta.newneoboardbuddy.domain.member.rpository;

import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserIdAndWorkspaceId(AuthUser authUser, Long workspaceId);

}
