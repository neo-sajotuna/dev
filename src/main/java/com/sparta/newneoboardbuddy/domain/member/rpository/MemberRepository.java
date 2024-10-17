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
    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);

    boolean existsByUserAndWorkspace(User user, Workspace workspace);

    Page<Member> findAllByUser(User user, Pageable pageable);

    /**
     * Member Table에서 user Id를 갖고 있는 Workspace 소속 유저를 workspace fetch Join한 상태로 반환하는 메서드
     * @param userId 찾고자 하는 user Id
     * @param workspaceId 소속을 확인할 workspace Id
     * @return userId를 갖고 있는 workspace fetch 상태의 member객체
     */
    @Query("select m from Member m join fetch m.workspace w  " +
            "where m.user.id = :userId AND m.workspace.spaceId = :workspaceId")
    Optional<Member> findByUserIdWithJoinFetchWorkspace(Long userId, Long workspaceId);
}
