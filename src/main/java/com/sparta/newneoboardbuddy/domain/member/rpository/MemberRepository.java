package com.sparta.newneoboardbuddy.domain.member.rpository;

import com.sparta.newneoboardbuddy.domain.member.entity.Member;
import com.sparta.newneoboardbuddy.domain.user.entity.User;
import com.sparta.newneoboardbuddy.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m " +
            "where m.memberId = :userId AND m.workspace.spaceId = :workspaceId")
    Optional<Member> findByUserIdWithJoinFetchWorkspace(Long userId, Long workspaceId);

    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);


    boolean existsByUserAndWorkspace(User user, Workspace workspace);

    Page<Member> findAllByUser(User user, Pageable pageable);

}
